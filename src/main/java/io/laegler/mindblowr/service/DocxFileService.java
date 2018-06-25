package io.laegler.mindblowr.service;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import io.laegler.mindblowr.util.TreeNode;
import io.laegler.mindblowr.util.TreeNode.TreeNodeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
@Service
public class DocxFileService {

	public Optional<TreeNode> parseFile(File file) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file.getName());
			TreeNode rootNode = read(inputStream, file.getName());
			inputStream.close();
			return ofNullable(rootNode);
		} catch (Exception e) {
			log.error("Failed to read input file: {} (docx)", file.getName(), e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				log.error("Failed to read input file: {} (docx)", file.getName(), e);
			}
		}
		return empty();
	}

	public Optional<TreeNode> parseMultipart(InputStream inputStream) {
		String filename = "test";
		try {
			TreeNode rootNode = read(inputStream, filename);
			inputStream.close();
			return ofNullable(rootNode);
		} catch (Exception e) {
			log.error("Failed to read input file: {}", filename, e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				log.error("Failed to read input file: {}", filename, e);
			}
		}
		return empty();
	}

	public Optional<TreeNode> parseMultipart(MultipartFile file) {
		BufferedInputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(file.getInputStream());
			TreeNode rootNode = read(inputStream, file.getOriginalFilename());
			inputStream.close();
			return ofNullable(rootNode);
		} catch (Exception e) {
			log.error("Failed to read input file: {}", file.getOriginalFilename(), e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				log.error("Failed to read input file: {}", file.getOriginalFilename(), e);
			}
		}
		return empty();
	}

	private TreeNode read(InputStream inputStream, String title) throws IOException {
		XWPFDocument document = new XWPFDocument(inputStream);
		// XWPFWordExtractor extractor = new XWPFWordExtractor(document);

		TreeNode rootNode = TreeNode.builder()//
				.title(title)//
				.name(title)//
				.level(0)//
				.build();

		TreeNode currentNode;

		for (XWPFParagraph p : document.getParagraphs()) {
			TreeNodeBuilder nodeBuilder = TreeNode.builder()//
					.title(title)//
					.name(title);
			if (p.getStyleID() != null) {
				if (p.getStyleID().equalsIgnoreCase("Heading0") || p.getStyleID().contains("Titel")) {
					log.debug("Found Node Level 0 (root)");
					TreeNode node = nodeBuilder.level(0).build();
					rootNode = node;
					currentNode = rootNode;
				} else if (p.getStyleID().equalsIgnoreCase("Heading1") || p.getStyleID().contains("berschrift1")) {
					log.debug("Found Node Level 1");
					TreeNode node = nodeBuilder.level(1).build();
					rootNode.addChild(node);
					currentNode = node;
				} else if (p.getStyleID().equalsIgnoreCase("Heading2") || p.getStyleID().contains("berschrift2")) {
					log.debug("Found Node Level 2");
					TreeNode node = nodeBuilder.level(2).build();
					rootNode.addChild(node);
					currentNode = node;
				} else if (p.getStyleID().equalsIgnoreCase("Heading3") || p.getStyleID().contains("berschrift3")) {
					log.debug("Found Node Level 3");
					TreeNode node = nodeBuilder.level(3).build();
					rootNode.addChild(node);
					currentNode = node;
				} else if (p.getStyleID().equalsIgnoreCase("Heading4") || p.getStyleID().contains("berschrift4")) {
					log.debug("Found Node Level 4");
					TreeNode node = nodeBuilder.level(4).build();
					rootNode.addChild(node);
					currentNode = node;
				}
			}
		}

		// document.getParagraphs().stream().forEach(p -> {
		// log.debug(p.getText());
		// log.debug(p.getParagraphText());
		// BigInteger level = p.getNumIlvl();
		// log.debug("Level: " + level);
		// rootNode.addChild(TreeNode.builder()//
		// .hash(sha256Hex(p.getParagraphText()))//
		// .title(title)//
		// .name(p.getParagraphText())//
		// .level(level)//
		// .build());
		// });

		return rootNode;
	}

}
