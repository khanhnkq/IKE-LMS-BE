/**
 * Spring Modulith application root.
 * Bounded context modules (finance, academic, notification, audit, platform)
 * nằm cùng cấp package com.ike.* — Spring Modulith tự detect module boundary.
 *
 * @see com.ike.finance.config.FinanceModuleConfiguration
 * @see com.ike.academic.config.AcademicModuleConfiguration
 * @see com.ike.notification.config.NotificationModuleConfiguration
 * @see com.ike.audit.config.AuditModuleConfiguration
 */
package com.ike.app;
