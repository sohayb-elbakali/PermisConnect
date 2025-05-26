import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  TouchableOpacity,
  ScrollView,
  Alert,
} from 'react-native';
import Header from '../components/Header';
import { useRouter } from 'expo-router';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface Question {
  id: number;
  question: string;
  options: string[];
  correctAnswer: number;
}

const questions: Question[] = [
  {
    id: 1,
    question: "Quelle est la vitesse maximale autorisée en agglomération?",
    options: ["30 km/h", "50 km/h", "70 km/h", "90 km/h"],
    correctAnswer: 1
  },
  {
    id: 2,
    question: "À quelle distance minimale d'un passage piéton doit-on s'arrêter?",
    options: ["5 mètres", "10 mètres", "15 mètres", "20 mètres"],
    correctAnswer: 0
  },
  {
    id: 3,
    question: "Quelle est la signification du panneau triangulaire rouge?",
    options: ["Danger", "Obligation", "Interdiction", "Information"],
    correctAnswer: 0
  },
  {
    id: 4,
    question: "Quand doit-on allumer les feux de croisement?",
    options: ["Seulement la nuit", "La nuit et par mauvais temps", "Toujours", "Jamais"],
    correctAnswer: 1
  },
  {
    id: 5,
    question: "Quelle est la distance de sécurité minimale à respecter?",
    options: ["1 seconde", "2 secondes", "3 secondes", "4 secondes"],
    correctAnswer: 1
  }
];

export default function BlancTestScreen() {
  const router = useRouter();
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState<number[]>([]);
  const [showResults, setShowResults] = useState(false);

  const handleAnswer = (answerIndex: number) => {
    const newAnswers = [...selectedAnswers];
    newAnswers[currentQuestion] = answerIndex;
    setSelectedAnswers(newAnswers);

    if (currentQuestion < questions.length - 1) {
      setCurrentQuestion(currentQuestion + 1);
    } else {
      setShowResults(true);
    }
  };

  const calculateScore = () => {
    const correctAnswers = selectedAnswers.filter(
      (answer, index) => answer === questions[index].correctAnswer
    ).length;
    return (correctAnswers / questions.length) * 100;
  };

  const handleRestart = () => {
    setCurrentQuestion(0);
    setSelectedAnswers([]);
    setShowResults(false);
  };

  const handleFinish = async () => {
    const score = calculateScore();
    await AsyncStorage.setItem('lastBlancTestScore', String(score));
    Alert.alert(
      "Test Terminé",
      `Votre score est de ${score}%`,
      [
        {
          text: "Recommencer",
          onPress: handleRestart
        },
        {
          text: "Retour",
          onPress: () => router.back()
        }
      ]
    );
  };

  if (showResults) {
    return (
      <SafeAreaView style={styles.container}>
        <Header title="Résultats" />
        <ScrollView style={styles.content}>
          <View style={styles.resultsContainer}>
            <Text style={styles.scoreText}>
              {calculateScore()}%
            </Text>
            {questions.map((question, index) => (
              <View key={question.id} style={styles.resultItem}>
                <Text style={styles.questionText}>
                  {question.question}
                </Text>
                <Text style={[
                  styles.answerText,
                  selectedAnswers[index] === question.correctAnswer 
                    ? styles.correctAnswer 
                    : styles.wrongAnswer
                ]}>
                  Votre réponse: {question.options[selectedAnswers[index]]}
                </Text>
                {selectedAnswers[index] !== question.correctAnswer && (
                  <Text style={styles.correctAnswer}>
                    Bonne réponse: {question.options[question.correctAnswer]}
                  </Text>
                )}
              </View>
            ))}
          </View>
          <TouchableOpacity style={styles.button} onPress={handleFinish}>
            <Text style={styles.buttonText}>Terminer</Text>
          </TouchableOpacity>
        </ScrollView>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <Header title="Test Blanc" />
      <ScrollView style={styles.content}>
        <View style={styles.questionContainer}>
          <Text style={styles.questionNumber}>
            Question {currentQuestion + 1}/{questions.length}
          </Text>
          <Text style={styles.questionText}>
            {questions[currentQuestion].question}
          </Text>
          <View style={styles.optionsContainer}>
            {questions[currentQuestion].options.map((option, index) => (
              <TouchableOpacity
                key={index}
                style={[
                  styles.optionButton,
                  selectedAnswers[currentQuestion] === index && styles.selectedOption
                ]}
                onPress={() => handleAnswer(index)}
              >
                <Text style={[
                  styles.optionText,
                  selectedAnswers[currentQuestion] === index && styles.selectedOptionText
                ]}>
                  {option}
                </Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  content: {
    flex: 1,
    padding: 20,
  },
  questionContainer: {
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  questionNumber: {
    fontSize: 14,
    color: '#666',
    marginBottom: 10,
  },
  questionText: {
    fontSize: 18,
    fontWeight: '600',
    color: '#333',
    marginBottom: 20,
  },
  optionsContainer: {
    gap: 10,
  },
  optionButton: {
    backgroundColor: '#f8f9fa',
    padding: 15,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#e0e0e0',
  },
  selectedOption: {
    backgroundColor: '#ff6b35',
    borderColor: '#ff6b35',
  },
  optionText: {
    fontSize: 16,
    color: '#333',
  },
  selectedOptionText: {
    color: '#fff',
  },
  resultsContainer: {
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 20,
    marginBottom: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  scoreText: {
    fontSize: 48,
    fontWeight: 'bold',
    color: '#ff6b35',
    textAlign: 'center',
    marginBottom: 20,
  },
  resultItem: {
    marginBottom: 20,
    padding: 15,
    backgroundColor: '#f8f9fa',
    borderRadius: 8,
  },
  answerText: {
    fontSize: 16,
    marginTop: 5,
  },
  correctAnswer: {
    color: '#4CAF50',
  },
  wrongAnswer: {
    color: '#f44336',
  },
  button: {
    backgroundColor: '#ff6b35',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 10,
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
}); 