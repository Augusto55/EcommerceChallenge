package br.com.ecommercechallenge.controller;

import br.com.ecommercechallenge.config.SecurityConfig;
import br.com.ecommercechallenge.dto.ResourceCreationSuccessDto;
import br.com.ecommercechallenge.dto.ResponseMessageDto;
import br.com.ecommercechallenge.dto.cart.AddProductDto;
import br.com.ecommercechallenge.dto.cart.CartResponseDto;
import br.com.ecommercechallenge.dto.cart.ChangeProductQuantityDto;
import br.com.ecommercechallenge.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart", description = "Requests adding, removing and editing Products in the Shopping Cart")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasAuthority('SCOPE_Default') or #userId == authentication.name")
    @GetMapping
    public ResponseEntity<CartResponseDto> listCartProducts(Authentication authentication){
        String userid = authentication.getName();
        var cart = cartService.getCartForCurrentUser(userid);
        return ResponseEntity.ok().body(cart);
    }

    @PreAuthorize("hasAuthority('SCOPE_Default') or #userId == authentication.name")
    @PostMapping
    public ResponseEntity<ResourceCreationSuccessDto> addProductsToCart(Authentication authentication,
                                                             @RequestBody @Valid AddProductDto addProductDto){
        String userid = authentication.getName();
        var addedProduct = cartService.addProductToCart(userid, addProductDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResourceCreationSuccessDto("Product with successfully added to shopping cart. ",
                        addedProduct.getId().toString()));
    }

    @PreAuthorize("hasAuthority('SCOPE_Default') or #userId == authentication.name")
    @DeleteMapping("/{cartProductId}")
    public ResponseEntity<ResponseMessageDto> removeProductsFromCart(Authentication authentication,
                                                                             @PathVariable String cartProductId){
        String userid = authentication.getName();
        cartService.removeProductFromCart(userid, cartProductId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseMessageDto("Item removed from shopping cart."));
    }

    @PreAuthorize("hasAuthority('SCOPE_Default') or #userId == authentication.name")
    @PatchMapping("/{cartProductId}")
    public ResponseEntity<ResponseMessageDto> changeCartProductQuantity(Authentication authentication,
                                                                                @PathVariable String cartProductId,
                                                                        @RequestBody @Valid ChangeProductQuantityDto changeProductQuantityDto){
        String userid = authentication.getName();
        cartService.changeProductQuantity(userid, cartProductId, changeProductQuantityDto.quantity());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessageDto("Item quantity altered successfully."));
    }


}
