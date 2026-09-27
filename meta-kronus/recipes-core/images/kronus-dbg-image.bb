require recipes-core/images/kronus-dev-image.bb

IMAGE_FEATURES += "allow-empty-password allow-root-login empty-root-password post-install-logging dbg-pkgs"
