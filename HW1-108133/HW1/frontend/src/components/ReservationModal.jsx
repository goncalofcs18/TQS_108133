import React from "react";

export default function ReservationModal({ description, token, onClose }) {
    const copyToClipboard = () => {
        navigator.clipboard.writeText(token);
    };

    return (
        <div className="modal">
            <div className="modal-box">
                <strong>Reserva para: {description}</strong>
                <p className="success">
                    Reserva feita com sucesso! Código: <span className="code">{token}</span>
                    <button onClick={copyToClipboard}>Copiar</button>
                </p>
                <button className="close-btn" onClick={onClose}>
                    Fechar
                </button>
            </div>
        </div>
    );
}