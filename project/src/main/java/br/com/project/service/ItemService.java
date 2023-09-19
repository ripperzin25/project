package br.com.project.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.model.Item;

@Service
public class ItemService {

	private List<Item> itens;

	public void crateItemList() {
		if (itens == null) {
			itens = new ArrayList<Item>();
		}
	}

	public boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
	}

	private long parseId(JSONObject item) {
		return Long.valueOf((int) item.get("id"));
	}

	private BigDecimal parseValue(JSONObject item) {
		return new BigDecimal((String) item.get("value"));
	}

	private LocalDate parseDate(JSONObject item) {
		String startDate = (String) item.get("date");
		LocalDate date = LocalDate.parse(startDate);
		return date;
	}

	private String parseDescription(JSONObject item) {
		return (String) item.get("description");
	}

	private void setItemValues(JSONObject jsonItem, Item item) {
		item.setDescription(jsonItem.get("description") != null ? parseDescription(jsonItem) : item.getDescription());
		item.setDate(jsonItem.get("date") != null ? parseDate(jsonItem) : item.getDate());
		item.setValue(jsonItem.get("value") != null ? parseValue(jsonItem) : item.getValue());

	}

	public Item create(JSONObject jsonItem) {

		Item item = new Item();
		item.setId(parseId(jsonItem));
		setItemValues(jsonItem, item);

		return item;
	}

	public void add(Item item) {
		crateItemList();
		itens.add(item);
	}

	public List<Item> find() {
		crateItemList();
		return itens;
	}

	public Item findById(long id) {
		return itens.stream().filter(i -> id == i.getId()).collect(Collectors.toList()).get(0);
	}

}
