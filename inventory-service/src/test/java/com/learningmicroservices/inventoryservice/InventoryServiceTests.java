package com.learningmicroservices.inventoryservice;


import com.learningmicroservices.inventoryservice.dto.InventoryResponse;
import com.learningmicroservices.inventoryservice.model.Inventory;
import com.learningmicroservices.inventoryservice.repository.InventoryRepository;
import com.learningmicroservices.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTests {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testIsInStock() {
        List<Inventory> inventories = Arrays.asList(
                new Inventory(1L, "iphone_13", 10),
                new Inventory(2L, "iphone_13_red", 0)
        );
        when(inventoryRepository.findBySkuCodeIn(Arrays.asList("iphone_13", "iphone_13_red"))).thenReturn(inventories);

        List<InventoryResponse> response = inventoryService.isInStock(Arrays.asList("iphone_13", "iphone_13_red"));

        assertEquals(inventories.size(), response.size());
        for (int i = 0; i < inventories.size(); i++) {
            assertEquals(inventories.get(i).getSkuCode(), response.get(i).getSkuCode());
            assertEquals(inventories.get(i).getQuantity() > 0, response.get(i).isInStock());
        }
    }
}