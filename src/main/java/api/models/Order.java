package api.models;

import lombok.Data;
import java.util.List;

@Data
public class Order {
    private List<String> ingredients;
    private String _id;
    private String status;
    private String number;
    private String createdAt;
    private String updatedAt;
}