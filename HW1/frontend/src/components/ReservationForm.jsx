import { useState } from 'react';

function ReservationForm({ meal, onFinish }) {
    const [name, setName] = useState('');
    const [studentNumber, setStudentNumber] = useState('');
    const [success, setSuccess] = useState(null);
    const [token, setToken] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const res = await fetch("http://localhost:8080/api/reservations", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, studentNumber, mealId: meal.id })
        });

        if (res.ok) {
            const data = await res.json();
            setToken(data.token);
            setSuccess(true);
        } else {
            setSuccess(false);
        }
    };

    const copyToClipboard = () => {
        if (token) {
            navigator.clipboard.writeText(token);
        }
    };

    return (
        <div className="reservation-form">
            <h3>Reserva para: {meal.description}</h3>
            {success === null ? (
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="Nome completo"
                        required
                    />
                    <input
                        type="text"
                        value={studentNumber}
                        onChange={(e) => setStudentNumber(e.target.value)}
                        placeholder="Número mecanográfico"
                        required
                    />
                    <button type="submit">Confirmar Reserva</button>
                </form>
            ) : success ? (
                <div className="reservation-success">
                    Reserva feita com sucesso!<br />
                    Código: <strong>{token}</strong>
                    <button onClick={copyToClipboard}>Copiar</button>
                </div>
            ) : (
                <div className="reservation-error">
                    Erro ao fazer a reserva. Tenta novamente.
                </div>
            )}
            <div className="reservation-footer">
                <button onClick={onFinish} className="cancel">Fechar</button>
            </div>
        </div>
    );
}

export default ReservationForm;
