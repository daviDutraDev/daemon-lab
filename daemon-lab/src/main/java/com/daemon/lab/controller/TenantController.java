package com.daemon.lab.controller;

import com.daemon.lab.repository.TenantRepository;
import com.daemon.lab.model.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TenantController {

    private final TenantRepository tenantRepository;

    @PostMapping
    public ResponseEntity<Tenant> criar(@RequestBody Tenant tenant) {
        tenant.setCreatedAt(LocalDateTime.now());

        Tenant tenantSalvo = tenantRepository.save(tenant);

        return ResponseEntity.status(HttpStatus.CREATED).body(tenantSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Tenant>> listar() {
        List<Tenant> tenants = tenantRepository.findAll();

        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tenant> buscarPorId(@PathVariable Long id) {

        Optional<Tenant> tenant = tenantRepository.findById(id);

        if (tenant.isPresent()) {
            return ResponseEntity.ok(tenant.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tenant> atualizar(
            @PathVariable Long id,
            @RequestBody Tenant tenant) {

        Optional<Tenant> tenantExistente = tenantRepository.findById(id);

        if (tenantExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tenant tenantAtualizado = tenantExistente.get();

        tenantAtualizado.setNome(tenant.getNome());

        Tenant tenantSalvo = tenantRepository.save(tenantAtualizado);

        return ResponseEntity.ok(tenantSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        Optional<Tenant> tenant = tenantRepository.findById(id);

        if (tenant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        tenantRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
