package io.laegler.mindblowr.rest;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.http.ResponseEntity.ok;
import io.laegler.mindblowr.service.DocxFileService;
import io.laegler.mindblowr.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import javax.servlet.annotation.MultipartConfig;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Api(tags = "Mindblowr API")
@RestController
@RequestMapping(value = "/mind")
@MultipartConfig(fileSizeThreshold = 20971520)
public class MindController {

	@Autowired
	private DocxFileService docxService;

	@Autowired
	private ImageService imageService;

	@ApiOperation(value = "Blow your mind!", notes = "Get blown", code = 201)
	@ApiResponses({@ApiResponse(code = 201, message = "Successful blown mind"), @ApiResponse(code = 500, message = "Error blowing mind")})
	@ApiImplicitParams({
			@ApiImplicitParam(type = "file", name = "file", value = "docx/ppt/pdf file", required = true, dataType = "file", paramType = "form")})
	@PostMapping(value = "/blow", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<?> blowMind(
			@ApiParam(hidden = true) @ApiIgnore @RequestPart(name = "file", required = true) final MultipartFile file) throws IOException {
		log.trace("blowMind()");

		if (file == null) {
			throw new IllegalStateException("No File in Request");
		}
		File image = imageService.generateImage(//
				docxService.parseMultipart(file)//
						.orElseThrow(() -> new IllegalStateException("Could not read input File"))//
		);
		Path path = Paths.get(image.getAbsolutePath());
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ok()//
				.headers(headers)//
				.contentLength(image.length())//
				.contentType(parseMediaType(APPLICATION_OCTET_STREAM_VALUE))//
				.body(resource);
	}

	@ApiOperation(value = "Blow your mind faster!", notes = "Get blown faster", code = 201)
	@ApiResponses({@ApiResponse(code = 201, message = "Successful blown mind"), @ApiResponse(code = 500, message = "Error blowing mind")})
	@PostMapping(value = "/blow/shortcut")
	public ResponseEntity<?> blowMindShortcut() throws IOException {
		log.trace("blowMind()");

		InputStream inputStream = this.getClass().getResourceAsStream("test.docx");
		if (inputStream == null) {
			throw new IllegalStateException("No File in File Input Stream");
		}
		File image = imageService.generateSvg(//
				docxService.parseMultipart(inputStream)//
						.orElseThrow(() -> new IllegalStateException("Could not read input File"))//
		);
		Path path = Paths.get(image.getAbsolutePath());
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ok()//
				.headers(headers)//
				.contentLength(image.length())//
				.contentType(parseMediaType(APPLICATION_OCTET_STREAM_VALUE))//
				.body(resource);
	}

}
