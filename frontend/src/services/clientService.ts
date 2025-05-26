import apiClient from './api';

export interface ClientRequest {
  nom: string;
  prenom: string;
  email: string;
  password: string;
  telephone: string;
  adresse: string;
  dateNaissance: string;
  numeroPermis: string;
  typePermis: string;
}

export interface ClientResponse {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  adresse: string;
  dateNaissance: string;
  numeroPermis: string;
  typePermis: string;
}

class ClientService {
  async createClient(clientData: ClientRequest): Promise<ClientResponse> {
    try {
      console.log('Creating client with data:', clientData);
      const response = await apiClient.post<ClientResponse>('/clients', clientData);
      console.log('Client creation response:', response.data);
      return response.data;
    } catch (error: any) {
      console.error('Client creation error:', error.response?.data || error.message);
      throw error;
    }
  }

  async getClientById(id: number): Promise<ClientResponse> {
    try {
      const response = await apiClient.get<ClientResponse>(`/clients/${id}`);
      return response.data;
    } catch (error: any) {
      console.error(`Error fetching client ${id}:`, error);
      throw error;
    }
  }

  async updateClient(id: number, clientData: Partial<ClientRequest>): Promise<ClientResponse> {
    try {
      const response = await apiClient.put<ClientResponse>(`/clients/${id}`, clientData);
      return response.data;
    } catch (error: any) {
      console.error(`Error updating client ${id}:`, error);
      throw error;
    }
  }

  async assignUserToAutoEcole(userId: number, autoEcoleId: number): Promise<void> {
    try {
      await apiClient.put(`/clients/${userId}/auto-ecole`, {
        autoEcoleId: autoEcoleId
      });
    } catch (error: any) {
      console.error(`Error assigning user ${userId} to auto-école ${autoEcoleId}:`, error);
      throw error;
    }
  }
}

export default new ClientService();
