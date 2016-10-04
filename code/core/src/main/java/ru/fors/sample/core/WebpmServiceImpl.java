package ru.fors.sample.core;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Alexey Perminov
 * Date: 28.09.2010
 * Time: 21:38:35
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class WebpmServiceImpl implements WebpmService {
}
