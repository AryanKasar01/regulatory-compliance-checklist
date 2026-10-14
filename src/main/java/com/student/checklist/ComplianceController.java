package com.student.checklist;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/records")
@CrossOrigin
public class ComplianceController {
    public record ComplianceRecord(long id, String title, String owner, String status, String dueDate) {}
    public record RecordInput(String title, String owner, String status, String dueDate) {}
    private final Map<Long, ComplianceRecord> records = new LinkedHashMap<>();
    private final AtomicLong ids = new AtomicLong(0);

    public ComplianceController() {
        create(new RecordInput("Review access permissions", "IT Team", "Pending", "2026-10-20"));
        create(new RecordInput("Verify backup procedure", "Operations", "In Progress", "2026-10-22"));
        create(new RecordInput("Update privacy policy", "Compliance Team", "Complete", "2026-10-25"));
    }

    @GetMapping
    public synchronized List<ComplianceRecord> list(@RequestParam(required = false) String search) {
        return records.values().stream()
          .filter(r -> search == null || (r.title()+" "+r.owner()+" "+r.status()).toLowerCase().contains(search.toLowerCase()))
          .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized ComplianceRecord create(@RequestBody RecordInput input) {
        validate(input);
        long id = ids.incrementAndGet();
        ComplianceRecord r = new ComplianceRecord(id, input.title().trim(), input.owner().trim(), input.status(), input.dueDate());
        records.put(id, r);
        return r;
    }

    @PutMapping("/{id}")
    public synchronized ComplianceRecord update(@PathVariable long id, @RequestBody RecordInput input) {
        if (!records.containsKey(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
        validate(input);
        ComplianceRecord r = new ComplianceRecord(id, input.title().trim(), input.owner().trim(), input.status(), input.dueDate());
        records.put(id, r);
        return r;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public synchronized void delete(@PathVariable long id) {
        if (records.remove(id) == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found");
    }

    private void validate(RecordInput input) {
        if (input == null || input.title() == null || input.title().isBlank()
            || input.owner() == null || input.owner().isBlank()
            || input.status() == null || !Set.of("Pending", "In Progress", "Complete").contains(input.status())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title, owner and valid status are required");
        }
    }
}
