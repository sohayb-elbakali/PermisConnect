import axios from 'axios';

export interface Moniteur {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  autoEcoleId: number;
}

export interface TimeSlotDisplay {
  id: number;
  time: string;
  instructor: string;
  status: string;
  available: boolean;
}

class MoniteurService {
  private baseUrl = 'http://localhost:8080/api';

  async getMoniteursByAutoEcole(autoEcoleId: number): Promise<Moniteur[]> {
    try {
      const response = await axios.get(`${this.baseUrl}/moniteurs/auto-ecole/${autoEcoleId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching moniteurs:', error);
      throw error;
    }
  }

  async getMoniteurTimeSlots(moniteurId: number): Promise<TimeSlotDisplay[]> {
    try {
      const response = await axios.get(`${this.baseUrl}/time-slots/moniteur/${moniteurId}`);
      // Flatten and map the deeply nested response
      return response.data.map((slot: any) => ({
        id: slot.id,
        time: `${slot.startTime?.substring(11, 16)} - ${slot.endTime?.substring(11, 16)}`,
        instructor: slot.moniteur ? `${slot.moniteur.prenom} ${slot.moniteur.nom}` : '',
        status: slot.status,
        available: slot.status === 'AVAILABLE',
      }));
    } catch (error) {
      console.error('Error fetching time slots:', error);
      throw error;
    }
  }
}

export default new MoniteurService(); 