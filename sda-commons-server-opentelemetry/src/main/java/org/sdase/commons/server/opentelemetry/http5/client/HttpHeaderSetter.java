/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sdase.commons.server.opentelemetry.http5.client;

import io.opentelemetry.context.propagation.TextMapSetter;
import jakarta.annotation.Nullable;
import org.apache.hc.core5.http.HttpRequest;

enum HttpHeaderSetter implements TextMapSetter<HttpRequest> {
  INSTANCE;

  @Override
  public void set(@Nullable HttpRequest carrier, String key, String value) {
    if (carrier == null) {
      return;
    }
    carrier.setHeader(key, value);
  }
}
