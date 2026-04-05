# Cargo Manifest Validator - Python Implementation

def normalizeUnits(manifest):
    """
    Normalizes the weight to kilograms without mutating the original manifest
    
    Args:
        manifest (dict): The cargo manifest object
        
    Returns:
        dict: A new manifest object with weight in kg
    """
    # Create a copy of the manifest to avoid mutation
    normalized = manifest.copy()
    
    # Convert pounds to kilograms if necessary
    if normalized.get("unit") == "lb":
        normalized["weight"] = normalized["weight"] * 0.45
        normalized["unit"] = "kg"
    
    return normalized


def validateManifest(manifest):
    """
    Validates a cargo manifest and returns validation errors
    
    Args:
        manifest (dict): The cargo manifest object
        
    Returns:
        dict: An object containing missing/invalid properties, or empty if valid
    """
    errors = {}
    
    # Check containerId: must be a positive integer
    if "containerId" not in manifest:
        errors["containerId"] = "Missing"
    elif not isinstance(manifest["containerId"], int) or manifest["containerId"] <= 0:
        errors["containerId"] = "Invalid"
    
    # Check destination: must be a non-empty string after trimming
    if "destination" not in manifest:
        errors["destination"] = "Missing"
    elif not isinstance(manifest["destination"], str) or manifest["destination"].strip() == "":
        errors["destination"] = "Invalid"
    
    # Check weight: must be a positive number (not NaN)
    if "weight" not in manifest:
        errors["weight"] = "Missing"
    elif not isinstance(manifest["weight"], (int, float)) or manifest["weight"] != manifest["weight"] or manifest["weight"] <= 0:
        # manifest["weight"] != manifest["weight"] checks for NaN (NaN != NaN is True)
        errors["weight"] = "Invalid"
    
    # Check unit: must be "kg" or "lb"
    if "unit" not in manifest:
        errors["unit"] = "Missing"
    elif manifest["unit"] not in ["kg", "lb"]:
        errors["unit"] = "Invalid"
    
    # Check hazmat: must be a boolean
    if "hazmat" not in manifest:
        errors["hazmat"] = "Missing"
    elif not isinstance(manifest["hazmat"], bool):
        errors["hazmat"] = "Invalid"
    
    return errors


def processManifest(manifest):
    """
    Processes a cargo manifest, logging validation results
    
    Args:
        manifest (dict): The cargo manifest object
    """
    errors = validateManifest(manifest)
    is_valid = len(errors) == 0
    
    if is_valid:
        print(f"Validation success: {manifest['containerId']}")
        normalized = normalizeUnits(manifest)
        print(f"Total weight: {normalized['weight']} kg")
    else:
        print(f"Validation error: {manifest.get('containerId')}")
        print(errors)


# Example usage and testing
if __name__ == "__main__":
    # Test normalizeUnits
    test_manifest = {
        "containerId": 68,
        "destination": "Salinas",
        "weight": 101,
        "unit": "lb",
        "hazmat": True
    }
    
    print("Original:", test_manifest)
    normalized = normalizeUnits(test_manifest)
    print("Normalized:", normalized)
    print("Original after normalization (should be unchanged):", test_manifest)
    
    print("\n" + "="*60 + "\n")
    
    # Test validateManifest with valid manifest
    valid_manifest = {
        "containerId": 1,
        "destination": "Santa Cruz",
        "weight": 304,
        "unit": "kg",
        "hazmat": False
    }
    print("Validating valid manifest:", validateManifest(valid_manifest))
    
    # Test validateManifest with empty object
    print("Validating empty manifest:", validateManifest({}))
    
    # Test validateManifest with invalid containerId
    invalid_container = {
        "containerId": None,
        "destination": "Santa Cruz",
        "weight": 304,
        "unit": "kg",
        "hazmat": False
    }
    print("Validating invalid containerId:", validateManifest(invalid_container))
    
    # Test processManifest
    print("\n" + "="*60 + "\n")
    print("Processing valid manifest:")
    processManifest({
        "containerId": 55,
        "destination": "Carmel",
        "weight": 400,
        "unit": "lb",
        "hazmat": False
    })
    
    print("\nProcessing invalid manifest:")
    processManifest({
        "containerId": -88,
        "destination": "Soledad",
        "weight": float('nan')
    })
