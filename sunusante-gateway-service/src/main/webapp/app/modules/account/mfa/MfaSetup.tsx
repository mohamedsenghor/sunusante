import React, { useState } from 'react';
import { Button, FormGroup, Label, Input, Alert } from 'reactstrap';
import axios from 'axios';
import { QRCodeSVG } from 'qrcode.react';

export const MfaSetup = () => {
  const [secret, setSecret] = useState(null);
  const [otpCode, setOtpCode] = useState('');
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const setupMfa = async () => {
    try {
      const response = await axios.post('/api/account/mfa/setup');
      setSecret(response.data);
      setError(null);
    } catch (err) {
      console.log(err);
      setError('Erreur lors de l\'initialisation du MFA: ' + err.message);
    }
  };

  const validateMfa = async () => {
    try {
      const response = await axios.post('/api/account/mfa/validate', parseInt(otpCode, 10), {
        headers: { 'Content-Type': 'application/json' }
      });
      if (response.data === true) {
        setSuccess(true);
        setError(null);
      } else {
        setError('Code invalide.');
      }
    } catch (err) {
      setError('Erreur lors de la validation.');
    }
  };

  return (
    <div className="p-4">
      <h2>Configuration du MFA</h2>
      {!secret && <Button color="primary" onClick={setupMfa}>Initialiser le MFA</Button>}

      {secret && (
        <div>
          <p>Scannez ce QR Code avec Google Authenticator :</p>
          <QRCodeSVG value={`otpauth://totp/SunuSante:${secret}?secret=${secret}&issuer=SunuSante`} />
          <p>Secret: {secret}</p>

          <FormGroup>
            <Label for="otp">Entrez le code de votre application :</Label>
            <Input type="text" id="otp" value={otpCode} onChange={e => setOtpCode(e.target.value)} />
          </FormGroup>
          <Button color="success" onClick={validateMfa}>Valider</Button>
        </div>
      )}

      {error && <Alert color="danger" className="mt-2">{error}</Alert>}
      {success && <Alert color="success" className="mt-2">MFA activé avec succès !</Alert>}
    </div>
  );
};

export default MfaSetup;
