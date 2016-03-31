package org.jb.common.security.encoder;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.BasePasswordEncoder;

public class MD5Encoder extends BasePasswordEncoder {

	public String encodePassword(String origPwd, Object salt)
			throws DataAccessException {
		String saltStr = (String)salt;
		return MD5.getMD5ofStr(origPwd+saltStr);
	}

	public boolean isPasswordValid(String encPwd, String origPwd, Object salt)
			throws DataAccessException {
		return encPwd.equals(encodePassword(origPwd, salt));
	}

}
