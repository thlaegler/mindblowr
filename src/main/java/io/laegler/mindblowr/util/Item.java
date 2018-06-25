package io.laegler.mindblowr.util;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.math.BigInteger;

@Data
@Builder
public class Item implements Serializable {

	private static final long serialVersionUID = 1713731278036373014L;

	private String hash;

	private String title;

	private String name;

	private String description;

	private BigInteger level;

}
