package br.com.project.controller;

import java.net.URI;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.project.model.Item;
import br.com.project.service.ItemService;

@RestController
@RequestMapping("/project/purchase")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@GetMapping
	public ResponseEntity<List<Item>> find() {
		if (itemService.find().isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(itemService.find());
	}

	@PostMapping
	@ResponseBody
	public ResponseEntity<Item> create(@RequestBody JSONObject item) {
		try {
			if (itemService.isJSONValid(item.toString())) {
				Item itemCreated = itemService.create(item);
				URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path(String.valueOf(itemCreated.getId()))
						.build().toUri();

				itemService.add(itemCreated);
				return ResponseEntity.created(uri).body(null);

			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}

}
