package br.com.fiap.mottooth.config;

import br.com.fiap.mottooth.model.*;
import br.com.fiap.mottooth.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private static String sanitizePhone(String raw) {
        if (raw == null) return null;
        String onlyDigits = raw.replaceAll("\\D+", "");
        return onlyDigits.isEmpty() ? null : onlyDigits;
    }

    private static String normalizePlaca(String placa) {
        return placa == null ? null : placa.trim().toUpperCase(Locale.ROOT);
    }

    @Bean
    public CommandLineRunner loadData(
            ModeloMotoRepository modeloMotoRepository,
            ModeloBeaconRepository modeloBeaconRepository,
            ClienteRepository clienteRepository,
            MotoRepository motoRepository,
            BeaconRepository beaconRepository,
            PatioRepository patioRepository,
            LocalizacaoRepository localizacaoRepository
    ) {
        return args -> {

            // =========================
            // MODELOS DE MOTO
            // =========================
            ModeloMoto modelo1 = modeloMotoRepository
                    .findFirstByNomeAndFabricante("Sport 110i", "Mottu")
                    .orElseGet(() -> {
                        ModeloMoto m = new ModeloMoto();
                        m.setNome("Sport 110i");
                        m.setFabricante("Mottu");
                        return modeloMotoRepository.save(m);
                    });

            ModeloMoto modelo2 = modeloMotoRepository
                    .findFirstByNomeAndFabricante("CG 160", "Honda")
                    .orElseGet(() -> {
                        ModeloMoto m = new ModeloMoto();
                        m.setNome("CG 160");
                        m.setFabricante("Honda");
                        return modeloMotoRepository.save(m);
                    });

            // =========================
            // MODELOS DE BEACON
            // =========================
            ModeloBeacon beaconModelo1 = modeloBeaconRepository
                    .findFirstByNomeAndFabricante("BLE Tracker", "Mottu Tech")
                    .orElseGet(() -> {
                        ModeloBeacon b = new ModeloBeacon();
                        b.setNome("BLE Tracker");
                        b.setFabricante("Mottu Tech");
                        return modeloBeaconRepository.save(b);
                    });

            ModeloBeacon beaconModelo2 = modeloBeaconRepository
                    .findFirstByNomeAndFabricante("iBeacon Pro", "Apple")
                    .orElseGet(() -> {
                        ModeloBeacon b = new ModeloBeacon();
                        b.setNome("iBeacon Pro");
                        b.setFabricante("Apple");
                        return modeloBeaconRepository.save(b);
                    });

            // =========================
            // CLIENTES
            // =========================
            String cpf1 = "123.456.789-00";
            String email1 = "joao.silva@email.com".toLowerCase(Locale.ROOT);
            Cliente cliente1 = clienteRepository.findByCpf(cpf1)
                    .orElseGet(() -> {
                        Cliente c = new Cliente();
                        c.setNome("João Silva");
                        c.setCpf(cpf1);
                        c.setEmail(email1);
                        c.setTelefone(sanitizePhone("(11) 98765-4321"));
                        try {
                            return clienteRepository.save(c);
                        } catch (DataIntegrityViolationException ex) {
                            return clienteRepository.findByEmailIgnoreCase(email1)
                                    .orElseThrow(() -> new IllegalStateException(
                                            "Falha ao localizar cliente após violação (email1).", ex));
                        }
                    });

            String cpf2 = "987.654.321-00";
            String email2 = "maria.oliveira@email.com".toLowerCase(Locale.ROOT);
            Cliente cliente2 = clienteRepository.findByCpf(cpf2)
                    .orElseGet(() -> {
                        Cliente c = new Cliente();
                        c.setNome("Maria Oliveira");
                        c.setCpf(cpf2);
                        c.setEmail(email2);
                        c.setTelefone(sanitizePhone("(11) 91234-5678"));
                        try {
                            return clienteRepository.save(c);
                        } catch (DataIntegrityViolationException ex) {
                            return clienteRepository.findByEmailIgnoreCase(email2)
                                    .orElseThrow(() -> new IllegalStateException(
                                            "Falha ao localizar cliente após violação (email2).", ex));
                        }
                    });

            // =========================
            // MOTOS (idempotente por placa – case-insensitive)
            // =========================
            String p1 = normalizePlaca("ABC1234");
            Moto moto1 = motoRepository.findByPlacaIgnoreCase(p1)
                    .orElseGet(() -> {
                        Moto m = new Moto();
                        m.setPlaca(p1);
                        m.setCliente(cliente1);
                        m.setModeloMoto(modelo1);
                        m.setDataRegistro(LocalDateTime.now());
                        return motoRepository.save(m);
                    });

            String p2 = normalizePlaca("XYZ9876");
            Moto moto2 = motoRepository.findByPlacaIgnoreCase(p2)
                    .orElseGet(() -> {
                        Moto m = new Moto();
                        m.setPlaca(p2);
                        m.setCliente(cliente2);
                        m.setModeloMoto(modelo2);
                        m.setDataRegistro(LocalDateTime.now());
                        return motoRepository.save(m);
                    });

            // =========================
            // BEACONS (idempotente por UUID e moto)
            // =========================
            Beacon b1 = beaconRepository.findByUuid("uuid-001-ABC")
                    .orElseGet(() -> beaconRepository.findFirstByMoto_Id(moto1.getId())
                            .orElseGet(() -> {
                                Beacon b = new Beacon();
                                b.setUuid("uuid-001-ABC");
                                b.setBateria(85);
                                b.setMoto(moto1);
                                b.setModeloBeacon(beaconModelo1);
                                return beaconRepository.save(b);
                            }));

            Beacon b2 = beaconRepository.findByUuid("uuid-002-XYZ")
                    .orElseGet(() -> beaconRepository.findFirstByMoto_Id(moto2.getId())
                            .orElseGet(() -> {
                                Beacon b = new Beacon();
                                b.setUuid("uuid-002-XYZ");
                                b.setBateria(92);
                                b.setMoto(moto2);
                                b.setModeloBeacon(beaconModelo2);
                                return beaconRepository.save(b);
                            }));

            // =========================
            // PÁTIOS (idempotente por nome)
            // =========================
            Patio patio1 = patioRepository.findFirstByNome("Pátio Central")
                    .orElseGet(() -> {
                        Patio p = new Patio();
                        p.setNome("Pátio Central");
                        return patioRepository.save(p);
                    });

            Patio patio2 = patioRepository.findFirstByNome("Pátio Zona Sul")
                    .orElseGet(() -> {
                        Patio p = new Patio();
                        p.setNome("Pátio Zona Sul");
                        return patioRepository.save(p);
                    });

            // =========================
            // LOCALIZAÇÕES (evita duplicar por moto+timestamp)
            // =========================
            createLocalizacaoIfMissing(localizacaoRepository, moto1, patio1,
                    new BigDecimal("10.5"), new BigDecimal("20.3"), LocalDateTime.now().minusHours(2));

            createLocalizacaoIfMissing(localizacaoRepository, moto1, patio1,
                    new BigDecimal("15.7"), new BigDecimal("25.9"), LocalDateTime.now().minusHours(1));

            createLocalizacaoIfMissing(localizacaoRepository, moto2, patio2,
                    new BigDecimal("30.2"), new BigDecimal("40.8"), LocalDateTime.now());
        };
    }

    private static void createLocalizacaoIfMissing(
            LocalizacaoRepository repo, Moto moto, Patio patio,
            BigDecimal x, BigDecimal y, LocalDateTime ts
    ) {
        Optional<Localizacao> exists = repo.findFirstByMotoAndDataHora(moto, ts);
        if (exists.isPresent()) return;

        Localizacao loc = new Localizacao();
        loc.setPosicaoX(x);
        loc.setPosicaoY(y);
        loc.setDataHora(ts);
        loc.setMoto(moto);
        loc.setPatio(patio);
        repo.save(loc);
    }
}
