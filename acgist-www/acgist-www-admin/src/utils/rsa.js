import NodeRSA from 'node-rsa';
import service from './request';

var rsa = NodeRSA({ b: 2048 });

// rsa.encrypt(message, 'base64');
service.get('/rsa/private/key').then(response => {
  rsa.importKey(`-----BEGIN PUBLIC KEY-----\n${response.message}\n-----END PUBLIC KEY-----`);
  rsa.setOptions({ encryptionScheme: 'pkcs1' }); // RSA/ECB/PKCS1Padding
});

export default rsa;
