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
      // Handle both DTO and nested moniteur object
      return response.data.map((slot: any) => {
        let instructor = 'Unknown Instructor';
        if (slot.instructor) {
          instructor = slot.instructor;
        } else if (slot.moniteur && (slot.moniteur.prenom || slot.moniteur.nom)) {
          instructor = `${slot.moniteur.prenom || ''} ${slot.moniteur.nom || ''}`.trim();
        }
        return {
          id: slot.id,
          time: `${slot.startTime?.substring(11, 16)} - ${slot.endTime?.substring(11, 16)}`,
          instructor,
          status: slot.status,
          available: slot.status === 'AVAILABLE',
        };
      });
    } catch (error) {
      console.error('Error fetching time slots:', error);
      throw error;
    }
  }
}

export default new MoniteurService(); 