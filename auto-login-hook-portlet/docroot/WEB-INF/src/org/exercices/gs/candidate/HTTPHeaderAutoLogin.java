package org.exercices.gs.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exercices.gs.candidate.constants.HTTPLoginConstants;

import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

public class HTTPHeaderAutoLogin implements AutoLogin {

	@Override
	public String[] handleException(HttpServletRequest request,
			HttpServletResponse response, Exception ex)
			throws AutoLoginException {
		return null;
	}

	@Override
	public String[] login(HttpServletRequest request,
			HttpServletResponse response) throws AutoLoginException {

		String[] credentials = null;

		String login = (String) request
				.getHeader(HTTPLoginConstants.HTTP_HEADER_AUTH_LOGIN_HEADER);

		if (login != null) {

			long companyId = PortalUtil.getCompanyId(request);
			User user = null;

			try {
				Role role = RoleLocalServiceUtil.getRole(companyId, HTTPLoginConstants.ROLE_NAME);

				try {
					user = UserLocalServiceUtil.getUserByScreenName(companyId,
							login);

				} catch (Exception exception) {
					return credentials;
				}

				RoleLocalServiceUtil.addUserRole(user.getUserId(),
						role.getRoleId());

				credentials = new String[3];

				credentials[0] = String.valueOf(user.getUserId());
				credentials[1] = user.getPassword();
				credentials[2] = Boolean.TRUE.toString();

			} catch (Exception e) {
				return credentials;
			}

		}
		return credentials;
	}
}
