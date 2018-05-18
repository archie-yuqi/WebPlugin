package com.pub.internal.hybrid;

import java.util.Map;

public interface ConfigParser {

    Config parse(Map<String, Object> metaData) throws HybridException;
}
