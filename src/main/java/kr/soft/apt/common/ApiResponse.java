package kr.soft.apt.common;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private int status;          // HTTP 상태코드
    private String message;      // 응답 메시지
    private T data;              // 실제 데이터
    private LocalDateTime timestamp; // 응답 시간

    // ✅ 성공 응답 (200)
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    // ✅ 성공 응답 (200)
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    // ✅ 실패 응답 (400)
    public static <T> ResponseEntity<ApiResponse<T>> error(String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
