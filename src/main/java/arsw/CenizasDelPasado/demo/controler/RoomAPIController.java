package arsw.CenizasDelPasado.demo.controler;


import arsw.CenizasDelPasado.demo.model.Room;
import arsw.CenizasDelPasado.demo.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/v1/rooms")
public class RoomAPIController {
    @Autowired
    private RoomService roomService;

    @Operation(summary = "Obtener todas las salas", description = "Este endpoint devuelve una lista de todas las salas.")
    @ApiResponse(responseCode = "200", description = "Lista de salas", content = @Content)
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> protocolGetRoom(){
        try {
            return new ResponseEntity<>(roomService.showAllRooms(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Obtener una sala por código", description = "Este endpoint devuelve una sala específica basada en su código.")
    @ApiResponse(responseCode = "302", description = "Sala encontrada", content = @Content)
    @ApiResponse(responseCode = "404", description = "Sala no encontrada", content = @Content)
    @GetMapping(value = "/{code}")
    public ResponseEntity<?> getRoom(@PathVariable("code") String code) {
        try {
            return new ResponseEntity<>(roomService.getRoom(code), HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Obtener estadísticas de una sala", description = "Este endpoint devuelve las estadísticas de una sala basada en su código como unJSON del objeto RoomStats.")
    @ApiResponse(responseCode = "302", description = "Estadísticas de la sala", content = @Content)
    @ApiResponse(responseCode = "404", description = "Sala no encontrada", content = @Content)
    @GetMapping(value = "/{code}/room-stats")
    public ResponseEntity<?> getRoomStats(@PathVariable("code") String code) {
        try {
            return new ResponseEntity<>(roomService.getRoomStats(code), HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Obtener usuarios de una sala", description = "Este endpoint devuelve la lista de usuarios de una sala basada en su código.")
    @ApiResponse(responseCode = "302", description = "Usuarios en la sala", content = @Content)
    @ApiResponse(responseCode = "404", description = "Sala no encontrada", content = @Content)
    @GetMapping(value = "/{code}/users-in-room")
    public ResponseEntity<?> getRoomUsers(@PathVariable("code") String code) {
        try {
            return new ResponseEntity<>(roomService.getRoomUsers(code), HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Obtener niveles de una sala", description = "Este endpoint devuelve la lista de niveles de una sala basada en su código, esto sera un lista de identificadores de Level donde se guardan la persistencia de cada nivel.")
    @ApiResponse(responseCode = "302", description = "Lista de niveles de la sala", content = @Content)
    @ApiResponse(responseCode = "404", description = "Sala no encontrada", content = @Content)
    @GetMapping(value = "/{code}/levels")
    public ResponseEntity<?> getRoomLevels(@PathVariable("code") String code) {
        try {
            return new ResponseEntity<>(roomService.getRoomLevels(code), HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Crear una nueva sala por inyección", description = "Este endpoint permite crear una nueva sala, recibiendo un body con todos los parametros para la sala")
    @ApiResponse(responseCode = "201", description = "Sala creada exitosamente", content = @Content)
    @ApiResponse(responseCode = "406", description = "No se pudo crear la sala", content = @Content)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> protocolPostRoom(@RequestBody Room room){
        try{
            roomService.saveRoom(room);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch(Exception ex){
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @Operation(summary = "Crear una nueva sala desde cero con nombre de servidor", description = "Este endpoint permite crear una nueva sala con un nombre de servidor creando los demas parametros automaticamente.")
    @ApiResponse(responseCode = "201", description = "Sala creada exitosamente", content = @Content)
    @ApiResponse(responseCode = "406", description = "No se pudo crear la sala", content = @Content)
    @PostMapping(value = "/create")
    public ResponseEntity<?> createRoom(@RequestParam("server_name") String server_name){
        try{
            String code = roomService.generateCode();
            Room room = new Room(server_name,code);
            roomService.saveRoom(room);
            return new ResponseEntity<>(new String[]{code},HttpStatus.ACCEPTED);
        } catch(Exception ex){
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @Operation(summary = "Actualizar código de una sala", description = "Este endpoint permite actualizar el código de una sala. Devuelve el nuevo codigo generado, GUARDENLO.")
    @ApiResponse(responseCode = "202", description = "Código de sala actualizado exitosamente", content = @Content)
    @ApiResponse(responseCode = "406", description = "No se pudo actualizar el código de la sala", content = @Content)
    @PutMapping(value = "/{code}/new-code")
    public ResponseEntity<?> putRoomNewCode(@PathVariable("code") String code){
        try{
            return new ResponseEntity<>(roomService.updateRoomCode(code),HttpStatus.ACCEPTED);
        } catch(Exception ex){
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @Operation(summary = "Actualizar nombre de servidor de una sala", description = "Este endpoint permite actualizar el nombre de servidor de una sala.")
    @ApiResponse(responseCode = "202", description = "Nombre de servidor de sala actualizado exitosamente", content = @Content)
    @ApiResponse(responseCode = "406", description = "No se pudo actualizar el nombre de servidor de la sala", content = @Content)
    @PutMapping(value = "/{code}/update-name")
    public ResponseEntity<?> putRoomServerName(@PathVariable("code") String code,@RequestParam("server_name") String server_name){
        try{
            roomService.updateRoomServerName(code, server_name);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch(Exception ex){
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @Operation(summary = "Actualizar usuarios de una sala", description = "Este endpoint permite actualizar los usuarios de una sala.")
    @ApiResponse(responseCode = "202", description = "Usuarios de sala actualizados exitosamente", content = @Content)
    @ApiResponse(responseCode = "406", description = "No se pudo actualizar los usuarios de la sala", content = @Content)
    @PutMapping(value = "/{code}/update-users")
    public ResponseEntity<?> putRoomUsers(@PathVariable("code") String code, @RequestBody List<String> users){
        try{
            roomService.updateRoomUsers(code,users);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch(Exception ex){
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @Operation(summary = "Actualizar niveles de una sala", description = "Este endpoint permite actualizar los niveles de una sala.")
    @ApiResponse(responseCode = "202", description = "Niveles de sala actualizados exitosamente", content = @Content)
    @ApiResponse(responseCode = "406", description = "No se pudo actualizar los niveles de la sala", content = @Content)
    @PutMapping(value = "/{code}/update-levels")
    public ResponseEntity<?> putRoomLevels(@PathVariable("code") String code, @RequestBody List<Long> levels){
        try{
            roomService.updateRoomLevels(code,levels);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch(Exception ex){
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @Operation(summary = "Eliminar una sala", description = "Este endpoint permite eliminar una sala.")
    @ApiResponse(responseCode = "202", description = "Sala eliminada exitosamente", content = @Content)
    @ApiResponse(responseCode = "406", description = "No se pudo eliminar la sala", content = @Content)
    @DeleteMapping(path = "/{code}/delete")
    public ResponseEntity<?> deleteRoom(@PathVariable("code") String code){
        try{
            roomService.deleteRoom(code);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch(Exception ex){
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
