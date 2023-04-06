plugins {
	`java-library`
	id("net.civmc.civgradle.plugin")
	id("io.papermc.paperweight.userdev") version "1.3.1"
}

civGradle {
	paper {
		pluginName = "NameLayer"
	}
}

dependencies {
	paperDevBundle("1.19.4-R0.1-SNAPSHOT")

	compileOnly("net.civmc.civmodcore:civmodcore-paper:2.3.5:dev-all")
}
