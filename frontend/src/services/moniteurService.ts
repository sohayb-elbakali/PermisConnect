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
  bookedBy?: number;
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
          bookedBy: slot.clientId || slot.bookedBy,
        };
      });
    } catch (error) {
      console.error('Error fetching time slots:', error);
      throw error;
    }
  }

  async updateTimeSlotStatus(timeSlotId: number, status: 'AVAILABLE' | 'BOOKED' | 'CANCELLED'): Promise<TimeSlotDisplay> {
    try {
      const response = await axios.put(`${this.baseUrl}/time-slots/${timeSlotId}/status`, null, {
        params: { status }
      });
      
      const slot = response.data;
      let instructor = 'Unknown Instructor';
      if (slot.moniteur && (slot.moniteur.prenom || slot.moniteur.nom)) {
        instructor = `${slot.moniteur.prenom || ''} ${slot.moniteur.nom || ''}`.trim();
      }
      
      return {
        id: slot.id,
        time: `${slot.startTime?.substring(11, 16)} - ${slot.endTime?.substring(11, 16)}`,
        instructor,
        status: slot.status,
        available: slot.status === 'AVAILABLE',
        bookedBy: slot.clientId || slot.bookedBy,
      };
    } catch (error) {
      console.error('Error updating time slot status:', error);
      throw error;
    }
  }

  async getReservationsByClient(clientId: number): Promise<{ timeSlotId: number }[]> {
    try {
      const response = await axios.get(`${this.baseUrl}/reservations/client/${clientId}`);
      // Return an array of objects with at least timeSlotId
      return response.data.map((reservation: any) => ({
        timeSlotId: reservation.timeSlot?.id || reservation.timeSlotId
      }));
    } catch (error) {
      console.error('Error fetching reservations for client:', error);
      throw error;
    }
  }
}

export default new MoniteurService(); 