package api.multipartes.dev.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(
    name = "invoices", indexes = [
        Index(name = "idx_ticket_folio", columnList = "ticket_folio"),
        Index(name = "idx_receiver", columnList = "receiver_customer"),
        Index(name = "idx_invoice_number", columnList = "invoice_number")
    ]
)
data class Invoice(
    @Id
    @Column(columnDefinition = "CHAR(36)")
    val folio: String = java.util.UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_folio", referencedColumnName = "folio")
    val ticket: Ticket? = null,

    @Column(name = "invoice_number", nullable = false, length = 50)
    @NotBlank(message = "Invoice number is required")
    val invoiceNumber: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_customer")
    val receiverCustomer: CustomerInvoiceData? = null,

    @Column(name = "url_document", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "URL document is required")
    val urlDocument: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now()
)
