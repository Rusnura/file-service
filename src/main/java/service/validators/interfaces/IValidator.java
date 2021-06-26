package service.validators.interfaces;

public interface IValidator {
  <T> boolean validate(T object);
}
