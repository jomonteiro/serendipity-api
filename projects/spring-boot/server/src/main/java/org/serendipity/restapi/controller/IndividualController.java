package org.serendipity.restapi.controller;

import org.serendipity.restapi.hateoas.IndividualRepresentationModelAssembler;
import org.serendipity.restapi.model.Individual;
import org.serendipity.restapi.service.IndividualService;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@BasePathAwareController
public class IndividualController {

  private final IndividualRepresentationModelAssembler assembler;
  private final IndividualService entityService;

  public IndividualController(IndividualService entityService,
                              IndividualRepresentationModelAssembler assembler) {

    this.entityService = entityService;
    this.assembler = assembler;
  }
  
  @GetMapping("/whoami")
  public String whoami(@AuthenticationPrincipal Jwt jwt) {
    return String.format("Hello, %s!", jwt.getSubject());
  }
  
  @GetMapping("/individuals")
  @PreAuthorize("hasAuthority('SCOPE_individual:read')")
  public ResponseEntity<CollectionModel<EntityModel<Individual>>> findAll() {
    
    return ResponseEntity.ok(assembler.toCollectionModel(entityService.findAll()));
  }
  
  @GetMapping("/individuals/{id}")
  @PreAuthorize("hasAuthority('SCOPE_individual:read')")
  public ResponseEntity<EntityModel<Individual>> findById(
      @PathVariable("id") final Long id) throws ResponseStatusException {
    
    Individual entity = entityService.findById(id).orElseThrow(() -> 
        new ResponseStatusException(HttpStatus.NOT_FOUND));
    
    return ResponseEntity.ok(assembler.toModel(entity));
  }
  
}

// @RestController
// @RequestMapping("/api")

// @Autowired
