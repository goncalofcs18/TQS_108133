import { useState } from "react";
import RestaurantSelector from "./components/RestaurantSelector";
import MealList from "./components/MealList";
import ReservationForm from "./components/ReservationForm";
import "./App.css";

function App() {
    const [restaurantId, setRestaurantId] = useState(null);
    const [selectedMeal, setSelectedMeal] = useState(null);

    return (
        <div className="app-container">
            <div className="card">
                <h1 className="header">MoliceiroMeals</h1>

                <RestaurantSelector onSelect={id => setRestaurantId(parseInt(id))} />

                {restaurantId && (
                    <MealList
                        restaurantId={restaurantId}
                        onSelectMeal={setSelectedMeal}
                    />
                )}

                {selectedMeal && (
                    <ReservationForm
                        meal={selectedMeal}
                        onFinish={() => setSelectedMeal(null)}
                    />
                )}
            </div>
        </div>
    );
}

export default App;