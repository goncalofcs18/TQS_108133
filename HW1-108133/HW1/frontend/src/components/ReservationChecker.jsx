import { useState } from 'react';

function ReservationChecker({ onBack, apiConfig }) {
    const [token, setToken] = useState('');
    const [reservation, setReservation] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [cancelSuccess, setCancelSuccess] = useState(false);
    const baseUrl = apiConfig?.baseUrl || '/api';

    const handleCheck = async () => {
        if (!token) return;
        
        setLoading(true);
        setError(null);
        setReservation(null);
        setCancelSuccess(false);
        
        try {
            const res = await fetch(`${baseUrl}/reservations/${token}`);
            
            if (res.ok) {
                const data = await res.json();
                setReservation(data);
            } else {
                setError('Reserva não encontrada. Verifique o código e tente novamente.');
            }
        } catch (error) {
            setError('Erro ao verificar reserva. Tente novamente mais tarde.');
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = async () => {
        if (!token || !reservation) return;
        
        setLoading(true);
        setError(null);
        
        try {
            const res = await fetch(`${baseUrl}/reservations/${token}`, {
                method: 'DELETE'
            });
            
            if (res.ok) {
                const data = await res.json();
                if (data.cancelled) {
                    setCancelSuccess(true);
                    setReservation(null);
                } else {
                    setError('Não foi possível cancelar a reserva. Ela pode já ter sido utilizada.');
                }
            } else {
                setError('Erro ao cancelar reserva. Tente novamente mais tarde.');
            }
        } catch (error) {
            setError('Erro ao cancelar reserva. Tente novamente mais tarde.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="reservation-checker">
            <h2>Verificar Reserva</h2>
            <p>Insira o código da reserva para verificar seus detalhes ou cancelá-la.</p>
            
            <div className="verification-form">
                <input
                    type="text"
                    value={token}
                    onChange={(e) => setToken(e.target.value)}
                    placeholder="Código da reserva"
                />
                <button 
                    onClick={handleCheck} 
                    disabled={loading || !token}
                >
                    {loading ? 'Verificando...' : 'Verificar'}
                </button>
            </div>
            
            {error && (
                <div className="verification-result error">
                    <p>{error}</p>
                </div>
            )}
            
            {cancelSuccess && (
                <div className="verification-result success">
                    <p>Reserva cancelada com sucesso!</p>
                </div>
            )}
            
            {reservation && (
                <div className="reservation-details">
                    <h3>Detalhes da Reserva</h3>
                    <p><strong>Nome:</strong> {reservation.name}</p>
                    <p><strong>Número de Estudante:</strong> {reservation.studentNumber}</p>
                    <p><strong>Refeição:</strong> {reservation.meal.description}</p>
                    <p><strong>Data:</strong> {new Date(reservation.meal.date).toLocaleDateString('pt-PT')}</p>
                    <p><strong>Restaurante:</strong> {reservation.meal.restaurant.name}</p>
                    <p><strong>Localização:</strong> {reservation.meal.restaurant.location}</p>
                    <p><strong>Estado:</strong> {reservation.used ? 'Utilizada' : 'Não utilizada'}</p>
                    
                    {!reservation.used && (
                        <button 
                            className="cancel-reservation-btn" 
                            onClick={handleCancel}
                            disabled={loading}
                        >
                            {loading ? 'Cancelando...' : 'Cancelar Reserva'}
                        </button>
                    )}
                </div>
            )}
        </div>
    );
}

export default ReservationChecker; 