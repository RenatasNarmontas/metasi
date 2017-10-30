package com.renosoftware.metasi.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Intercept exceptions
 */
@ControllerAdvice
public class ExceptionHandlerController {

  /**
   * Error handler
   *
   * @param e intercepted exception
   * @param redirectAttributes pass parameters to page
   * @return redirect to main page
   */
  @ExceptionHandler(value = Exception.class)
  public String defaultErrorHandler(Exception e, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", e.getMessage());
    return "redirect:/";
  }

}
