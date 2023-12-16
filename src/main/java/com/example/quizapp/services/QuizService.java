package com.example.quizapp.services;

import com.example.quizapp.dao.QuestionDao;
import com.example.quizapp.dao.QuizDao;
import com.example.quizapp.models.Question;
import com.example.quizapp.models.Quiz;
import com.example.quizapp.models.QuizQuestion;
import com.example.quizapp.models.QuizResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        try {
            List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestions(questions);
            quizDao.save(quiz);

            return new ResponseEntity<String>(title + " Quiz created successfully", HttpStatus.CREATED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<String>(title + " Quiz creation failed", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuizQuestion>> getQuizQuestions(Integer quizId) {
        try {
            Optional<Quiz> quiz = quizDao.findById(quizId);
            List<QuizQuestion> quizQuestions = new ArrayList<>();
            if (quiz.isPresent()) {
                List<Question> questionsFromDB = quiz.get().getQuestions();
                for (Question q: questionsFromDB) {
                    QuizQuestion qq = new QuizQuestion(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
                    quizQuestions.add(qq);
                }
            }
            return new ResponseEntity<>(quizQuestions, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> calculateResult(Integer quizId, List<QuizResponse> quizResponseList) {
        try {
            Optional<Quiz> quiz = quizDao.findById(quizId);
            int result = 0;
            if (quiz.isPresent()) {
                List<Question> questionsFromDB = quiz.get().getQuestions();
                int index = 0;

                for (QuizResponse quizResponse : quizResponseList) {
                    if (quizResponse.getResponse().equals(questionsFromDB.get(index).getRightAnswer())) {
                        result++;
                    }
                    index++;
                }
            }
            return new ResponseEntity<>(result + " / " + quizResponseList.size(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }
}
