try {
ObjectMapper objectMapper = new ObjectMapper();
String jsonString = objectMapper.writeValueAsString(list);
System.out.println("jsonString : "+jsonString);
} catch (JsonProcessingException e) {
}
