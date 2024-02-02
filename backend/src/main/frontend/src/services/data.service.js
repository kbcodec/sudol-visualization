import axios from 'axios';
import authHeader from './auth-header';

const API_URL = '/api/data/';

class DataService {
  postPublicContent(body) {
    return axios.post(API_URL + 'public', body);
  }

  postMapContent(body) {
    return axios.post(API_URL + 'map', body);
  }

  postTeacherContent(body) {
    return axios.post(API_URL + 'teacher', body, { headers: authHeader() });
  }

  postAdminContent(body) {
    return axios.post(API_URL + 'admin', body, { headers: authHeader() });
  }
  downloadCsv(body) {
    return axios.post(API_URL + 'downloadCsv', body, { headers: authHeader(), responseType: 'blob'})
  }

  downloadInstruction() {
    return axios.get(API_URL + 'downloadInstruction', { headers: authHeader(), responseType: 'blob'})
  }
}

export default new DataService();