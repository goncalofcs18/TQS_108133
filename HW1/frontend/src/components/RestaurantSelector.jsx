import { useEffect, useState } from 'react';

function RestaurantSelector({ onSelect }) {
    const [restaurants, setRestaurants] = useState([]);
    const [selected, setSelected] = useState("");

    useEffect(() => {
        fetch("http://localhost:8080/api/restaurants")
            .then((res) => res.json())
            .then((data) => {
                setRestaurants(data);
                if (data.length > 0) {
                    setSelected(data[0].id);
                    onSelect(data[0].id);
                }
            });
    }, []);

    const handleChange = (e) => {
        const value = e.target.value;
        setSelected(value);
        if (value) {
            onSelect(value);
        }
    };

    return (
        <>
            <label>Selecione uma unidade alimentar:</label>
            <select value={selected} onChange={handleChange}>
                {restaurants.map((r) => (
                    <option key={r.id} value={r.id}>
                        {r.name}
                    </option>
                ))}
            </select>
        </>
    );
}

export default RestaurantSelector;