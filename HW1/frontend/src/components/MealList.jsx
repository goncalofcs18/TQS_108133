import { useEffect, useState } from 'react';

const restaurantLocationMap = {
    1: 'aveiro',
    2: 'agueda',
    3: 'oiro',
    4: 'ilhavo'
};

function MealList({ restaurantId, onSelectMeal }) {
    const [meals, setMeals] = useState([]);
    const [weatherData, setWeatherData] = useState({}); // key: date, value: forecast

    useEffect(() => {
        if (!restaurantId) return;

        // Buscar refeições
        fetch(`http://localhost:8080/api/meals/restaurant/${restaurantId}`)
            .then(res => res.json())
            .then(async (mealsFetched) => {
                setMeals(mealsFetched);

                // Buscar meteorologia para cada data
                const location = restaurantLocationMap[restaurantId];
                if (!location) return;

                const today = new Date();
                const weatherByDate = {};

                for (const meal of mealsFetched) {
                    const mealDate = new Date(meal.date);
                    const daysAhead = Math.round((mealDate - today) / (1000 * 60 * 60 * 24));

                    if (daysAhead >= 0 && !weatherByDate[meal.date]) {
                        const res = await fetch(`http://localhost:8080/api/weather/${location}?daysAhead=${daysAhead}`);
                        const data = await res.json();
                        weatherByDate[meal.date] = data;
                    }
                }

                setWeatherData(weatherByDate);
            });
    }, [restaurantId]);

    const getWeatherForDate = (date) => {
        const weather = weatherData[date];
        if (!weather?.daily) return null;

        const formatDate = (d) => new Date(d).toISOString().split('T')[0];
        const formattedDate = formatDate(date);
        const index = weather.daily.time.findIndex(d => d === formattedDate);
        if (index === -1) return null;

        const max = weather.daily.temperature_2m_max?.[index];
        const min = weather.daily.temperature_2m_min?.[index];
        const code = parseInt(weather.daily.weathercode?.[index]);

        let icon = "❓";

        if ([0, 1].includes(code)) icon = "☀️"; // Clear
        else if ([2, 3].includes(code)) icon = "⛅"; // Cloudy
        else if ([45, 48].includes(code)) icon = "🌫️"; // Fog
        else if ([51, 53, 55, 56, 57].includes(code)) icon = "🌦️"; // Drizzle
        else if ([61, 63, 65, 66, 67].includes(code)) icon = "🌧️"; // Rain
        else if ([71, 73, 75, 77, 85, 86].includes(code)) icon = "❄️"; // Snow
        else if ([80, 81, 82].includes(code)) icon = "🌧️"; // Rain showers
        else if ([95].includes(code)) icon = "⛈️"; // Thunderstorm
        else if ([96, 99].includes(code)) icon = "🌩️"; // Storm with hail

        return { icon, min, max };
    };





    return (
        <div className="meal-list">
            <h2>Refeições disponíveis</h2>
            <div className="meal-grid">
                {meals.map(meal => {
                    const weatherInfo = getWeatherForDate(meal.date);
                    return (
                        <div className="meal-card" key={meal.id}>
                            <h3>{meal.date}</h3>
                            <p>{meal.description}</p>
                            {weatherInfo && (
                                <p className="weather">
                                    {weatherInfo.icon} {weatherInfo.min}°C - {weatherInfo.max}°C
                                </p>
                            )}
                            <button onClick={() => onSelectMeal(meal)}>Reservar</button>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}

export default MealList;
