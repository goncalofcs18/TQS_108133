import { useState } from 'react';

function StaffVerification({ apiConfig }) {
    const [token, setToken] = useState('');
    const [result, setResult] = useState(null);
    const [reservation, setReservation] = useState(null);
    const [loading, setLoading] = useState(false);
    const baseUrl = apiConfig?.baseUrl || '/api';

    const handleCheck = async () => {
        if (!token) return;
        
        setLoading(true);
        setResult(null);
        setReservation(null);
        
        try {
            // First, get the reservation details
            const getRes = await fetch(`${baseUrl}/reservations/${token}`);
            
            if (getRes.ok) {
                const data = await getRes.json();
                setReservation(data);
                
                if (data.used) {
                    setResult({ success: false, message: 'Esta reserva já foi utilizada.' });
                } else {
                    setResult({ success: true, message: 'Reserva válida!' });
                }
            } else {
                setResult({ success: false, message: 'Reserva não encontrada.' });
            }
        } catch (error) {
            setResult({ success: false, message: 'Erro ao verificar reserva.' });
        } finally {
            setLoading(false);
        }
    };

    const handleMarkAsUsed = async () => {
        setLoading(true);
        
        try {
            const res = await fetch(`${baseUrl}/reservations/${token}/check-in`, {
                method: 'POST'
            });
            
            if (res.ok) {
                const data = await res.json();
                if (data.used) {
                    setResult({ success: true, message: 'Reserva marcada como utilizada com sucesso!' });
                    if (reservation) {
                        setReservation({ ...reservation, used: true });
                    }
                } else {
                    setResult({ success: false, message: 'Não foi possível marcar a reserva como utilizada.' });
                }
            } else {
                setResult({ success: false, message: 'Erro ao marcar reserva como utilizada.' });
            }
        } catch (error) {
            setResult({ success: false, message: 'Erro ao marcar reserva como utilizada.' });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="staff-verification">
            <h2>Verificação de Reservas</h2>
            <p>Insira o código da reserva para verificar.</p>
            
            <div className="verification-form">
                <input
                    type="text"
                    value={token}
                    onChange={(e) => setToken(e.target.value)}
                    placeholder="Código da reserva"
                />
                <button onClick={handleCheck} disabled={loading || !token}>
                    {loading ? 'A verificar...' : 'Verificar'}
                </button>
            </div>
            
            {result && (
                <div className={`verification-result ${result.success ? 'success' : 'error'}`}>
                    <p>{result.message}</p>
                </div>
            )}
            
            {reservation && (
                <div className="reservation-details">
                    <h3>Detalhes da Reserva</h3>
                    <p><strong>Nome:</strong> {reservation.name}</p>
                    <p><strong>Número de Estudante:</strong> {reservation.studentNumber}</p>
                    <p><strong>Refeição:</strong> {reservation.meal.description}</p>
                    <p><strong>Data:</strong> {reservation.meal.date}</p>
                    <p><strong>Restaurante:</strong> {reservation.meal.restaurant.name}</p>
                    <p><strong>Localização:</strong> {reservation.meal.restaurant.location}</p>
                    <p><strong>Estado:</strong> {reservation.used ? 'Utilizada' : 'Não utilizada'}</p>
                    
                    {!reservation.used && (
                        <button 
                            className="mark-used-btn" 
                            onClick={handleMarkAsUsed}
                            disabled={loading}
                        >
                            {loading ? 'A processar...' : 'Marcar como Utilizada'}
                        </button>
                    )}
                </div>
            )}
        </div>
    );
}

export default StaffVerification; 