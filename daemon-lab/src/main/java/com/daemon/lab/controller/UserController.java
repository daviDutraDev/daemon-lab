package com.daemon.lab.controller;

import com.daemon.lab.model.User;
import com.daemon.lab.repository.TenantRepository;
import com.daemon.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    @PostMapping
    public ResponseEntity<User> criar(@RequestBody User user) {

        if (user.getTenant() == null || user.getTenant().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<?> tenant = tenantRepository.findById(user.getTenant().getId());

        if (tenant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User userSalvo = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userSalvo);
    }

    @GetMapping
    public ResponseEntity<List<User>> listar() {

        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> atualizar(
            @PathVariable Long id,
            @RequestBody User user) {

        Optional<User> userExistente = userRepository.findById(id);

        if (userExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User userAtualizado = userExistente.get();

        userAtualizado.setNome(user.getNome());
        userAtualizado.setEmail(user.getEmail());

        User userSalvo = userRepository.save(userAtualizado);

        return ResponseEntity.ok(userSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}