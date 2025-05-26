import apiClient from "./api";

export interface AutoEcole {
  id: number;
  nom: string;
  adresse: string;
  telephone: string;
  email: string;
  siret: string;
  codePostal: string;
  ville: string;
  siteWeb?: string;
  description?: string;
  horaires?: string;
}

class AutoEcoleService {
  async getAllAutoEcoles(): Promise<AutoEcole[]> {
    try {
      const response = await apiClient.get("/auto-ecoles");
      return response.data;
    } catch (error) {
      console.error("Error fetching auto-écoles:", error);
      throw error;
    }
  }

  async getAutoEcoleById(id: number): Promise<AutoEcole> {
    try {
      const response = await apiClient.get(`/auto-ecoles/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching auto-école ${id}:`, error);
      throw error;
    }
  }

  async assignUserToAutoEcole(userId: number, autoEcoleId: number): Promise<void> {
    try {
      await apiClient.put(`/clients/${userId}/auto-ecole`, {
        autoEcoleId: autoEcoleId
      });
    } catch (error) {
      console.error(`Error assigning user ${userId} to auto-école ${autoEcoleId}:`, error);
      throw error;
    }
  }
}

export default new AutoEcoleService(); 