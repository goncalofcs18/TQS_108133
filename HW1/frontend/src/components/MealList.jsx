import { useEffect, useState } from 'react';

const restaurantLocationMap = {
    1: 'aveiro',
    2: 'agueda',
    3: 'oiro',
    4: 'ilhavo'
};

function MealList({ restaurantId, onSelectMeal, apiConfig }) {
    const [meals, setMeals] = useState([]);
    const [weatherData, setWeatherData] = useState({}); // key: date, value: forecast
    const baseUrl = apiConfig?.baseUrl || '/api';

    useEffect(() => {
        if (!restaurantId) return;

        // Buscar refeições
        fetch(`${baseUrl}/meals/restaurant/${restaurantId}`)
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
                        try {
                            console.log(`Fetching weather for ${location}, days ahead: ${daysAhead}, date: ${meal.date}`);
                            const res = await fetch(`${baseUrl}/weather/${location}?daysAhead=${daysAhead}`);
                            if (res.ok) {
                                const data = await res.json();
                                console.log('Weather data received:', data);
                                weatherByDate[meal.date] = data;
                            } else {
                                console.error('Weather API returned error:', await res.text());
                            }
                        } catch (error) {
                            console.error('Error fetching weather:', error);
                        }
                    }
                }

                console.log('All weather data:', weatherByDate);
                setWeatherData(weatherByDate);
            })
            .catch(error => {
                console.error('Error fetching meals:', error);
            });
    }, [restaurantId, baseUrl]);

    const formatDateForApi = (dateString) => {
        const date = new Date(dateString);
        return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    };

    const getWeatherForDate = (date) => {
        const weather = weatherData[date];
        if (!weather?.daily) {
            console.log('No daily weather data found for date:', date);
            return null;
        }

        // The API returns exactly one date, so we can use index 0
        if (weather.daily.time.length === 0 || 
            !weather.daily.temperature_2m_max || 
            !weather.daily.temperature_2m_min || 
            !weather.daily.weathercode) {
            console.log('Incomplete weather data:', weather);
            return null;
        }

        const max = weather.daily.temperature_2m_max[0];
        const min = weather.daily.temperature_2m_min[0];
        const code = parseInt(weather.daily.weathercode[0]);

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

    const formatDisplayDate = (dateString) => {
        const options = { weekday: 'short', day: 'numeric', month: 'long' };
        return new Date(dateString).toLocaleDateString('pt-PT', options);
    };

    return (
        <div className="meal-list">
            <h2>Refeições disponíveis</h2>
            <div className="meal-grid">
                {meals.map(meal => {
                    const weatherInfo = getWeatherForDate(meal.date);
                    return (
                        <div className="meal-card" key={meal.id}>
                            <h3>{formatDisplayDate(meal.date)}</h3>
                            <p>{meal.description}</p>
                            {weatherInfo && (
                                <p className="weather">
                                    {weatherInfo.icon} {weatherInfo.min.toFixed(1)}°C - {weatherInfo.max.toFixed(1)}°C
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
