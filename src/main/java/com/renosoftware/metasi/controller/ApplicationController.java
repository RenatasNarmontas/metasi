package com.renosoftware.metasi.controller;

import com.renosoftware.metasi.storage.StorageService;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Main application controller
 */
@Controller
@Slf4j
public class ApplicationController {

  private final StorageService storageService;

  @Autowired
  public ApplicationController(StorageService storageService) {
    this.storageService = storageService;
  }

  /**
   * Serving main application page
   *
   * @param model pass data to page
   * @return page name
   * @throws IOException
   */
  @GetMapping("/")
  public String indexPage(Model model) throws IOException {
    model.addAttribute("uploadfiles", storageService.getUploadFiles().map(
        path -> MvcUriComponentsBuilder.fromMethodName(ApplicationController.class,
            "serverUploadFile", path.getFileName().toString()).build().toString())
        .collect(Collectors.toList())
    );
    model.addAttribute("downloadfiles", storageService.getDownloadFiles().map(
        path -> MvcUriComponentsBuilder.fromMethodName(ApplicationController.class,
            "serverDownloadFile", path.getFileName().toString()).build().toString())
        .collect(Collectors.toList())
    );
    return "index";
  }

  /**
   * Responsible to download uploaded file
   *
   * @param filename filename to download
   * @return as attachment
   */
  @GetMapping("/uploadfiles/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serverUploadFile(@PathVariable String filename) {
    Resource file = storageService.loadAsResourceUpload(filename);
      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  /**
   * Responsible to download generated file
   *
   * @param filename filename to download
   * @return file as attachment
   */
  @GetMapping("/downloadfiles/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serverDownloadFile(@PathVariable String filename) {
    Resource file = storageService.loadAsResourceDownload(filename);
      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  /**
   * Clear storage
   *
   * @param redirectAttributes
   * @return
   */
  @GetMapping("/storage/clear")
  public String storageDelete(RedirectAttributes redirectAttributes) {
    storageService.deleteAll();
    redirectAttributes.addFlashAttribute("message", "Storage cleared!");
    return "redirect:/";
  }

  /**
   * Responsible for file upload
   *
   * @param file file to upload and store in storage
   * @param redirectAttributes pass param about completion status
   * @return redirect to main page
   */
  @PostMapping(value = "/", params = {"upload"})
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
      RedirectAttributes redirectAttributes) {
    storageService.store(file);
    redirectAttributes.addFlashAttribute("message",
        "You successfully uploaded '" + file.getOriginalFilename() + "'!");

    return "redirect:/";
  }

  /**
   * Process uploaded files
   *
   * @param redirectAttributes pass param about completion status
   * @return redirect to main page
   */
  @GetMapping("/storage/process")
  public String process(RedirectAttributes redirectAttributes) {
    log.info("Processing files");

    storageService.process();

    redirectAttributes.addFlashAttribute("message", "Finished!");
    return "redirect:/";
  }

}
