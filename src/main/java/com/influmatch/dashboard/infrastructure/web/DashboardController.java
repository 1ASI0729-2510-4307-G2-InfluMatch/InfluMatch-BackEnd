package com.influmatch.dashboard.infrastructure.web;

import com.influmatch.dashboard.application.service.DashboardService;
import com.influmatch.dashboard.application.dto.DashboardInfluencerListDto;
import com.influmatch.dashboard.application.dto.DashboardBrandListDto;
import com.influmatch.dashboard.application.dto.DashboardInfluencerDetailDto;
import com.influmatch.dashboard.application.dto.DashboardBrandDetailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints para el dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @Operation(
        summary = "Listar influencers",
        description = "Obtiene una lista de perfiles resumidos de influencers"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de influencers obtenida exitosamente",
        content = @Content(schema = @Schema(implementation = DashboardInfluencerListDto.class))
    )
    @GetMapping("/influencers")
    public List<DashboardInfluencerListDto> listInfluencers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return dashboardService.listInfluencers(page, size);
    }

    @Operation(
        summary = "Listar marcas",
        description = "Obtiene una lista de perfiles resumidos de marcas"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de marcas obtenida exitosamente",
        content = @Content(schema = @Schema(implementation = DashboardBrandListDto.class))
    )
    @GetMapping("/brands")
    public List<DashboardBrandListDto> listBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return dashboardService.listBrands(page, size);
    }

    @Operation(
        summary = "Obtener detalle de influencer",
        description = "Obtiene el perfil completo de un influencer por su ID"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Perfil de influencer encontrado",
        content = @Content(schema = @Schema(implementation = DashboardInfluencerDetailDto.class))
    )
    @GetMapping("/influencers/{id}")
    public DashboardInfluencerDetailDto getInfluencerDetail(@PathVariable Long id) {
        return dashboardService.getInfluencerDetail(id);
    }

    @Operation(
        summary = "Obtener detalle de marca",
        description = "Obtiene el perfil completo de una marca por su ID"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Perfil de marca encontrado",
        content = @Content(schema = @Schema(implementation = DashboardBrandDetailDto.class))
    )
    @GetMapping("/brands/{id}")
    public DashboardBrandDetailDto getBrandDetail(@PathVariable Long id) {
        return dashboardService.getBrandDetail(id);
    }
} 