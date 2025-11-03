package com.example.quizapp.controller;

import com.example.quizapp.model.Attempt;
import com.example.quizapp.model.Quiz;
import com.example.quizapp.repository.AttemptRepository;
import com.example.quizapp.repository.QuizRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Controller
public class ReportController {

    @Autowired private AttemptRepository attemptRepo;
    @Autowired private QuizRepository quizRepo;

    @GetMapping("/quiz/{id}/attempts")
    public String viewAttempts(@PathVariable int id, Model model){
        Quiz quiz = quizRepo.findById(id).orElse(null);
        List<Attempt> attempts = attemptRepo.findByQuizId(id);
        model.addAttribute("quiz", quiz);
        model.addAttribute("attempts", attempts);
        return "attempts";
    }

    @GetMapping("/quiz/{id}/report")
    public void downloadReport(@PathVariable int id, HttpServletResponse response) throws IOException, DocumentException {
        Quiz quiz = quizRepo.findById(id).orElse(null);
        List<Attempt> attempts = attemptRepo.findByQuizId(id);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=quiz_report_" + id + ".pdf");

        Document doc = new Document();
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        doc.add(new Paragraph("Quiz Report: " + (quiz != null ? quiz.getTitle() : "N/A"), titleFont));
        doc.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.addCell(new PdfPCell(new Phrase("Student Name", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Score", headerFont)));
        table.addCell(new PdfPCell(new Phrase("Quiz ID", headerFont)));

        for(Attempt a : attempts){
            table.addCell(a.getStudentName());
            table.addCell(String.valueOf(a.getScore()));
            table.addCell(String.valueOf(a.getQuiz()!=null ? a.getQuiz().getId() : id));
        }
        doc.add(table);
        doc.close();
    }
}

