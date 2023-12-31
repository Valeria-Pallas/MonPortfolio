package com.example.controller;

import java.util.List;

import com.example.dto.NewUserDto;
import com.example.dto.ProjectDTO;
import com.example.dto.TaskDTO;
import com.example.dto.UserDTO;
import com.example.models.User;
import com.example.service.TaskService;
import com.example.service.UserService;
import com.example.util.NewUserDTOMapper;
import com.example.util.ProjectMapper;
import com.example.util.TaskMapper;
import com.example.util.UserMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private NewUserDTOMapper newUserMapper;

  @Autowired
  private ProjectMapper projectMapper;

  @Autowired
  private TaskService taskService;

  @Autowired
  private TaskMapper taskMapper;
  
  @Operation(description = "Create a new user")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "User created", content = @Content),
    @ApiResponse(responseCode = "400", description = "User with the same id exists", content = @Content),
    @ApiResponse(responseCode = "500", description = "Business error", content = @Content),
  })
  @PostMapping(value = "/create")
  public ResponseEntity<String> createUser(@RequestBody @Valid NewUserDto newUserDto) {
    User newUser = newUserMapper.mapToEntity(newUserDto);
    return new ResponseEntity<>(userService.addUser(newUser) ? HttpStatus.CREATED
                                                             : HttpStatus.BAD_REQUEST);
  }

  // TODO - Read all users - findAll
  // TODO - Add content to 2nd ApiResponse with a specific @Content? Or remove 2nd ApiResponse?
  @Operation(description = "Obtain information of all users")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found list of all users", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
    })
  })
  @GetMapping(value = "/all")
  public List<UserDTO> findAll() {
    List<User> allUsers = userService.getAllUsers();
    // TODO - add mapToDList
    return userMapper.mapToDtoList(allUsers);
  }

  // TODO - Read a user by id - findById
  @Operation(description = "Obtain information of a user by user id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "user found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
    }),
    @ApiResponse(responseCode = "404", description = "user not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
    })
  })
  @GetMapping(value = "/{id}")
  public UserDTO findById(@Parameter(description = "id of user to be searched") @PathVariable int id) {
    // TODO - User SecurityContextHolder to enforce user authorization
    return userMapper.mapToDto(userService.getUserById(id));
  }

  // TODO - Update a user by id - updateById
  @Operation(description = "Update information of a user")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "User updated", content = @Content),
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @PutMapping(value = "/{id}")
  public ResponseEntity<String> updateUser(@RequestBody @Valid UserDTO userDTO) {
    // TODO - User SecurityContextHolder to enforce user authorization
    // TODO:
    // if (userDTO.getName().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
    boolean success = userService.updateUser(userMapper.mapToEntity(userDTO));
    return new ResponseEntity<>(success ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
  }

  // TODO - Delete a user by id - deleteById
  @Operation(description = "Delete a user by id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "User deleted", content = @Content),
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
  })
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<String> deleteUser(@Parameter(description = "id of user to be deleted") @PathVariable int id) {
    // TODO - User SecurityContextHolder to enforce user authorization
    return new ResponseEntity<>(userService.deleteUser(id) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND); 
  }

  @Operation(description = "Obtain information of all projects of a user")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found list of projects of this user", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDTO.class))
    })
  })
  @GetMapping(value = "/{id}/projects")
  public List<ProjectDTO> findProjectsOfUser(@Parameter(description = "id of user") @PathVariable int id) {
    return projectMapper.mapToDtoList(userService.getAllProjectsByUserId(id));
  }

  @Operation(description = "Obtain information of all tasks of a user")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found list of tasks of this user", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))
    })
  })
  @GetMapping(value = "/{id}/tasks")
  public List<TaskDTO> findTasksOfUser(@Parameter(description = "id of user") @PathVariable int id) {
    return taskMapper.mapToDtoList(taskService.getAllTasksByUserId(id));
  }
}
