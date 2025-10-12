package api.multipartes.dev.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "invoices")
data class Invoice(
    @Id
    @Column(columnDefinition = "CHAR(36)")
    val folio: String = java.util.UUID.randomUUID().toString(),

    @ManyToOne
    @JoinColumn(name = "ticket_folio", referencedColumnName = "folio")
    val ticket: Ticket? = null,

    @Column(name = "invoice_number", nullable = false, length = 50)
    val invoiceNumber: String,

    @ManyToOne
    @JoinColumn(name = "receiver_customer", referencedColumnName = "rfc")
    val receiverCustomer: CustomerInvoiceData? = null,

    @Column(name = "url_document", nullable = false, columnDefinition = "TEXT")
    val urlDocument: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
