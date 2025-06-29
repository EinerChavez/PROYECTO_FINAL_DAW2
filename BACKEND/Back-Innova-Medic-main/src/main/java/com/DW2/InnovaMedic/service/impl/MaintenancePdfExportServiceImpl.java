package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.ListaRecetaDTO;
import com.DW2.InnovaMedic.service.MaintenancePdfExportService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;


@Service
public class MaintenancePdfExportServiceImpl implements MaintenancePdfExportService {

    @Override
    public byte[] exportarRecetaComoPdf(ListaRecetaDTO recetaDTO) throws Exception {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                        new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .create();

        Document documento = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(documento, baos);

        documento.open();
        Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLUE);
        Font subTituloFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Font negritaFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Paragraph titulo = new Paragraph("Clínica Velazquez\nReceta Médica", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        documento.add(titulo);
        documento.add(new Paragraph("Paciente: " + recetaDTO.nombrePaciente(), normalFont));
        documento.add(new Paragraph("Grupo sanguíneo: " + recetaDTO.grupoSanguineoPaciente(), normalFont));
        documento.add(new Paragraph("Fecha: " + recetaDTO.fecha(), normalFont));
        documento.add(new Paragraph("Médico: " + recetaDTO.nombreMedico(), normalFont));
        documento.add(new Paragraph("Especialidad: " + recetaDTO.especialidadMedico(), normalFont));
        documento.add(new Paragraph(" "));
        documento.add(new Paragraph("Diagnóstico:", subTituloFont));
        documento.add(new Paragraph(recetaDTO.diagnostico(), normalFont));
        documento.add(new Paragraph(" "));

        documento.add(new Paragraph("Tratamiento:", subTituloFont));
        documento.add(new Paragraph(recetaDTO.tratamiento(), normalFont));
        documento.add(new Paragraph(" "));

        documento.add(new Paragraph("Medicamentos recetados:", subTituloFont));
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell(new PdfPCell(new Phrase("N°", negritaFont)));
        table.addCell(new PdfPCell(new Phrase("Medicamento", negritaFont)));

        List<String> medicamentos = recetaDTO.medicamentos();
        for (int i = 0; i < medicamentos.size(); i++) {
            table.addCell(String.valueOf(i + 1));
            table.addCell(medicamentos.get(i));
        }
        documento.add(table);
        if (recetaDTO.instrucciones() != null && !recetaDTO.instrucciones().isBlank()) {
            documento.add(new Paragraph("Instrucciones adicionales:", subTituloFont));
            documento.add(new Paragraph(recetaDTO.instrucciones(), normalFont));
            documento.add(new Paragraph(" "));
        }

        documento.add(new Paragraph("Firma del médico: ____________________________", normalFont));

        documento.close();

        return baos.toByteArray();
    }
}
