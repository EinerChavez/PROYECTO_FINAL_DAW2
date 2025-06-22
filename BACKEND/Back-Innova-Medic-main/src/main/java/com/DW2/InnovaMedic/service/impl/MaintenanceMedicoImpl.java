package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.MedicoResumenDTO;
import com.DW2.InnovaMedic.dto.registro.MedicoRegistroDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.MedicoRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceMedicoImpl implements MaintenanceMedico {
    private final UsuarioRepository usuarioRepository;
    private final MedicoRepository medicoRepository;
    private final CitaRepository citaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registrarMedicos(MedicoRegistroDTO medicoRegistroDTO) throws Exception {
        usuarioRepository.findOneByEmail(medicoRegistroDTO.email())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario registrado con el email: " + medicoRegistroDTO.email());
                });

        Medico medico = new Medico();
        medico.setNombre(medicoRegistroDTO.nombre());
        medico.setApellido(medicoRegistroDTO.apellido());
        medico.setSexo(medicoRegistroDTO.sexo());
        medico.setTelefono(medicoRegistroDTO.telefono());
        medico.setEmail(medicoRegistroDTO.email());
        medico.setContrasenia(passwordEncoder.encode(medicoRegistroDTO.contrasenia()));
        medico.setEspecialidad(medicoRegistroDTO.especialidad());
        medico.setNumeroColegiado(medicoRegistroDTO.numeroColegiado());
        medico.setCodigoHospital(medicoRegistroDTO.codigoHospital());

        medicoRepository.save(medico);
    }

    @Override
    @Cacheable(value = "citasMedico")
    public List<CitaDTO> obtenerCitasMedico(Integer id) throws Exception {
        if (!medicoRepository.existsById(id)) {
            throw new IllegalArgumentException("Medico con Id " + id + " no existe");
        }

        List<Cita> citas = citaRepository.findByMedicoWithRecetasAndMedicamentos(id);

        return citas.stream()
                .map(cita -> CitaDTO.fromEntity(cita, cita.getReceta()))
                .toList();
    }


    @Override
    @Cacheable(value = "listaMedicos")
    public List<MedicoResumenDTO> listarMedicosResumen() throws Exception {
        List<Medico> medicos = medicoRepository.findAll();
        return medicos.stream()
                .map(MedicoResumenDTO::fromEntity)
                .toList();
    }
}
