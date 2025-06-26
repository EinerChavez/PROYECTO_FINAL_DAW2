package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.ListaRecetaDTO;

public interface MaintenancePdfExportService {
    public byte[] exportarRecetaComoPdf(ListaRecetaDTO recetaDTO) throws Exception;
}
