import NodeRSA from 'node-rsa';
import service from './request';

var rsa = { 'useable': false };

// rsa.encrypt(message, 'base64');
service.get('/rsa/private/key').then(response => {
  rsa.useable = true;
  rsa.publicKey = NodeRSA({ b: 2048 });
  rsa.publicKey.importKey(`-----BEGIN PUBLIC KEY-----\n${response.message}\n-----END PUBLIC KEY-----`);
  rsa.publicKey.setOptions({ encryptionScheme: 'pkcs1' }); // RSA/ECB/PKCS1Padding
});

export default rsa;
