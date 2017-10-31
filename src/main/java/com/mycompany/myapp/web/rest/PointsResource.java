package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Points;

import com.mycompany.myapp.repository.PointsRepository;
import com.mycompany.myapp.repository.search.PointsSearchRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.web.rest.vm.PointsPerWeek;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Points.
 */
@RestController
@RequestMapping("/api")
public class PointsResource {

    private final Logger log = LoggerFactory.getLogger(PointsResource.class);

    private static final String ENTITY_NAME = "points";

    private final PointsRepository pointsRepository;

    private final PointsSearchRepository pointsSearchRepository;

    public PointsResource(PointsRepository pointsRepository, PointsSearchRepository pointsSearchRepository) {
        this.pointsRepository = pointsRepository;
        this.pointsSearchRepository = pointsSearchRepository;
    }

    /**
     * POST  /points : Create a new points.
     *
     * @param points the points to create
     * @return the ResponseEntity with status 201 (Created) and with body the new points, or with status 400 (Bad Request) if the points has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/points")
    @Timed
    public ResponseEntity<Points> createPoints(@Valid @RequestBody Points points) throws URISyntaxException {
        log.debug("REST request to save Points : {}", points);
        if (points.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new points cannot already have an ID")).body(null);
        }
        Points result = pointsRepository.save(points);
        pointsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /points : Updates an existing points.
     *
     * @param points the points to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated points,
     * or with status 400 (Bad Request) if the points is not valid,
     * or with status 500 (Internal Server Error) if the points couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/points")
    @Timed
    public ResponseEntity<Points> updatePoints(@Valid @RequestBody Points points) throws URISyntaxException {
        log.debug("REST request to update Points : {}", points);
        if (points.getId() == null) {
            return createPoints(points);
        }
        Points result = pointsRepository.save(points);
        pointsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, points.getId().toString()))
            .body(result);
    }

    /**
     * GET  /points : get all the points.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of points in body
     */
    @GetMapping("/points")
    @Timed
    public List<Points> getAllPoints() {
        log.debug("REST request to get all Points");
        return pointsRepository.findAll();
        }

    /**
     * GET  /points/:id : get the "id" points.
     *
     * @param id the id of the points to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the points, or with status 404 (Not Found)
     */
    @GetMapping("/points/{id}")
    @Timed
    public ResponseEntity<Points> getPoints(@PathVariable Long id) {
        log.debug("REST request to get Points : {}", id);
        Points points = pointsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(points));
    }

    /**
     * DELETE  /points/:id : delete the "id" points.
     *
     * @param id the id of the points to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/points/{id}")
    @Timed
    public ResponseEntity<Void> deletePoints(@PathVariable Long id) {
        log.debug("REST request to delete Points : {}", id);
        pointsRepository.delete(id);
        pointsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/points?query=:query : search for the points corresponding
     * to the query.
     *
     * @param query the query of the points search
     * @return the result of the search
     */
    @GetMapping("/_search/points")
    @Timed
    public List<Points> searchPoints(@RequestParam String query) {
        log.debug("REST request to search Points for query {}", query);
        return StreamSupport
            .stream(pointsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    @GetMapping("/points-this-week")
    @Timed
    public ResponseEntity<PointsPerWeek> getPointsThisWeek(){
    	LocalDate now=LocalDate.now();
    	LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
    	LocalDate endOfWeek= now.with(DayOfWeek.SUNDAY);
    	log.debug("Busqueda de puntos entre: {} y {}",startOfWeek,endOfWeek);
    	List<Points> points = pointsRepository.findByPersonIsCurrentUser();
    	return calculatePoints(points);
    }
    
    private ResponseEntity<PointsPerWeek> calculatePoints( List<Points> points){
    	Integer numPoints= points.stream().mapToInt(p -> 1).sum();

    	PointsPerWeek count= new PointsPerWeek(LocalDate.now(), numPoints);
    	log.debug("Se devuelve count: {} y fecha {}", count.getPoints(),count.getWeek());
    	return new ResponseEntity<>(count,HttpStatus.OK);
    }
    
//    public ResponseEntity<List<Points>> getAllPoints(@ApiParam Pageable pageable){
//    	log.debug("Rest request to get a page of Points");
//    	Page<Points> page;
//    	if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
//    		page=pointsRepository.findAllByOrderByDateDesc(pageable);
//    		
//    	}else{
//    		page = pointsRepository.findByUserIsCurrentUser(pageable);
//    	}
//    	HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/points");
//    	return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }
}
