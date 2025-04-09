import { useState } from "react";
import RestaurantSelector from "./components/RestaurantSelector";
import MealList from "./components/MealList";
import ReservationForm from "./components/ReservationForm";
import StaffVerification from "./components/StaffVerification";
import ReservationChecker from "./components/ReservationChecker";
import "./App.css";

// Constants for API
const API_BASE_URL = '/api';

function App() {
    const [restaurantId, setRestaurantId] = useState(null);
    const [selectedMeal, setSelectedMeal] = useState(null);
    const [isStaffView, setIsStaffView] = useState(false);
    const [checkReservation, setCheckReservation] = useState(false);

    const resetState = () => {
        setSelectedMeal(null);
        setCheckReservation(false);
    };


    const apiConfig = {
        baseUrl: API_BASE_URL
    };

    return (
        <div className="app-container">
            <div className="card">
                <div className="header-container">
                    <h1 className="header">MoliceiroMeals</h1>
                    <div className="button-group">
                        <button 
                            className="toggle-btn"
                            onClick={() => {
                                setIsStaffView(!isStaffView);
                                resetState();
                            }}
                        >
                            {isStaffView ? "Modo Estudante" : "Modo Funcionário"}
                        </button>
                        {!isStaffView && !selectedMeal && (
                            <button 
                                className="toggle-btn check-btn"
                                onClick={() => setCheckReservation(!checkReservation)}
                            >
                                {checkReservation ? "Voltar" : "Verificar/Cancelar Reserva"}
                            </button>
                        )}
                    </div>
                </div>

                {isStaffView ? (
                    <StaffVerification apiConfig={apiConfig} />
                ) : (
                    <>
                        {checkReservation ? (
                            <ReservationChecker 
                                onBack={() => setCheckReservation(false)}
                                apiConfig={apiConfig}
                            />
                        ) : (
                            <>
                                {!selectedMeal && (
                                    <RestaurantSelector 
                                        onSelect={id => setRestaurantId(parseInt(id))}
                                        apiConfig={apiConfig}
                                    />
                                )}

                                {restaurantId && !selectedMeal && (
                                    <MealList
                                        restaurantId={restaurantId}
                                        onSelectMeal={setSelectedMeal}
                                        apiConfig={apiConfig}
                                    />
                                )}

                                {selectedMeal && (
                                    <ReservationForm
                                        meal={selectedMeal}
                                        onFinish={() => setSelectedMeal(null)}
                                        apiConfig={apiConfig}
                                    />
                                )}
                            </>
                        )}
                    </>
                )}
            </div>
        </div>
    );
}

export default App;