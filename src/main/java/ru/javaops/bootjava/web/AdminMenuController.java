package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Menu;
import ru.javaops.bootjava.service.MenuService;
import ru.javaops.bootjava.to.MenuTo;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "AdminMenuController", description = "Administrative operations with menu")
public class AdminMenuController {
    public static final String REST_URL = "/api/admin/restaurants/{id}/menus/{date}";

    private final MenuService menuService;

    public AdminMenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Operation(summary = "Adding the restaurant menu to date")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // 10
    public ResponseEntity<Menu> createMenu(
            @Parameter(description = "Restaurant id") @PathVariable int id,
            @Parameter(description = "Menu date") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Menu content") @RequestBody @Valid MenuTo menuTo) {
        Menu createdMenu = menuService.createMenu(id, date, menuTo);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(id, createdMenu.getMenuDate()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(createdMenu);
    }

    @Operation(summary = "Changing the menu of the restaurant to date")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // 11
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMenu(
            @Parameter(description = "Restaurant id") @PathVariable int id,
            @Parameter(description = "Menu date") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Menu content") @RequestBody @Valid MenuTo menuTo) {
        menuService.updateMenu(id, date, menuTo);
    }

    @Operation(summary = "Delete restaurant menu by date")
    @DeleteMapping // 12
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenu(
            @Parameter(description = "Restaurant id") @PathVariable int id,
            @Parameter(description = "Menu date") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        menuService.deleteMenu(id, date);
    }
}
